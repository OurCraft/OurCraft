package org.craft.modding;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import com.google.common.collect.*;
import com.google.common.reflect.*;
import com.google.common.reflect.ClassPath.ResourceInfo;
import com.google.gson.*;

import org.craft.*;
import org.craft.modding.events.*;
import org.craft.modding.script.lua.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.utils.io.*;
import org.reflections.*;
import org.slf4j.*;

public class AddonsLoader
{

    private HashMap<String, Object>                             addonInstances;
    private HashMap<Class<? extends Annotation>, IAddonManager> handlers;
    private EventBus                                            eventBus;
    private OurCraftInstance                                    game;
    private LuaEventBusListener                                 luaListener;
    private ClassLoader                                         classLoader;
    private List<AddonContainer>                                containers;
    private List<Class<?>>                                      loaded;
    private List<Class<? extends Annotation>>                   excluded;

    public AddonsLoader(OurCraftInstance gameInstance, EventBus eventBus)
    {
        addonInstances = Maps.newHashMap();
        containers = Lists.newArrayList();
        loaded = Lists.newArrayList();
        this.classLoader = ClassLoader.getSystemClassLoader();
        luaListener = new LuaEventBusListener();
        eventBus.addListener(luaListener);

        this.game = gameInstance;
        this.eventBus = eventBus;
        handlers = new HashMap<Class<? extends Annotation>, IAddonManager>();
        registerAddonAnnotation(Mod.class, new ModManager());

    }

    public void registerAddonAnnotation(Class<? extends Annotation> annot, IAddonManager<?> handler)
    {
        handlers.put(annot, handler);
    }

    public void loadAddon(Class<?> clazz) throws InstantiationException, IllegalAccessException
    {
        boolean added = false;
        //        if(loaded.contains(clazz))
        //            return;
        annotLoop: for(Class<? extends Annotation> c : handlers.keySet())
        {
            if(excluded != null && !excluded.isEmpty() && excluded.contains(c))
                continue;
            for(AddonContainer<?> container : containers)
            {
                if(container.getAddonAnnotation().annotationType() == c && container.getInstance().getClass() == clazz)
                {
                    added = true;
                    continue annotLoop;
                }
            }
            for(Annotation annot : clazz.getAnnotations())
            {
                if(annot.annotationType() == c)
                {
                    loaded.add(clazz);
                    IAddonManager<Annotation> manager = handlers.get(c);
                    IAddonHandler<Annotation> handler = manager.getHandler();
                    Object instance = clazz.newInstance();
                    for(Field f : clazz.getDeclaredFields())
                    {
                        if(f.isAnnotationPresent(Instance.class))
                        {
                            addonInstances.put(f.getAnnotation(Instance.class).value(), instance);
                            f.setAccessible(true);
                            f.set(null, instance);
                        }
                    }
                    AddonContainer<Annotation> container = handler.createContainer(annot, instance);
                    CommonHandler.setCurrentContainer(container);
                    manager.loadAddon(container);
                    eventBus.register(instance);

                    added = true;
                    Log.message("Loaded addon \"" + container.getName() + "\" as " + c.getSimpleName());
                    containers.add(container);
                    handler.onCreation(game, container);
                    break;
                }
            }
        }
        if(!added && (excluded == null || excluded.isEmpty()))
        {
            Log.error("Tried to register addon " + clazz.getName() + " but it is not supported");
        }
    }

    public void loadAll(File... folders)
    {
        for(File folder : folders)
        {
            File[] files = folder.listFiles();
            for(File file : files)
            {
                if(file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip"))
                {
                    try
                    {
                        DiskSimpleResourceLoader diskResLoader = new DiskSimpleResourceLoader();
                        ZipSimpleResourceLoader zipResLoader = new ZipSimpleResourceLoader(diskResLoader.getResource(new ResourceLocation("", file.getAbsolutePath())));
                        if(zipResLoader.doesResourceExists(new ResourceLocation("luaAddon.json")))
                        {
                            String rawJsonData = new String(zipResLoader.getResource(new ResourceLocation("luaAddon.json")).getData(), "UTF-8");
                            addLuaScript(rawJsonData, zipResLoader);
                            Gson gson = new Gson();
                            JsonObject jsonObject = gson.fromJson(rawJsonData, JsonObject.class);
                            if(jsonObject.has("id") && jsonObject.has("name") && jsonObject.has("version") && jsonObject.has("mainClass"))
                            {
                                String id = jsonObject.get("id").getAsString();
                                String name = jsonObject.get("name").getAsString();
                                String version = jsonObject.get("version").getAsString();
                                String mainClass = jsonObject.get("mainClass").getAsString();
                                String author = "unknown";
                                if(jsonObject.has("author"))
                                {
                                    author = jsonObject.get("author").getAsString();
                                }

                                Log.message("Found lua addon with id: " + id + ", name: " + name + ", version: " + version + " and mainClass: " + mainClass + ". Author is " + author);

                                LuaAddonContainer container = new LuaAddonContainer(id, name, version, author, mainClass);
                                CommonHandler.setCurrentContainer(container);
                                new LuaScript(zipResLoader.getResource(new ResourceLocation(mainClass)), luaListener, container, game);

                                eventBus.register(container);
                                File configFolder = new File(SystemUtils.getGameFolder(), "configs/");
                                if(!configFolder.exists())
                                {
                                    configFolder.mkdirs();
                                }
                                Logger logger = LoggerFactory.getLogger(container.getName());
                                ModPreInitEvent preInitEvent = new ModPreInitEvent(game, logger, new File(configFolder, container.getId() + ".cfg"), configFolder);
                                eventBus.fireEvent(preInitEvent, container, null);
                                containers.add(container);
                            }
                            else
                            {
                                Log.error("Missing data when loading lua addon: luaAddon.json must contain fields \"id\", \"name\", \"version\" and \"mainClass\"");
                            }

                            try
                            {
                                Method m = classLoader.getClass().getDeclaredMethod("addURL", URL.class);
                                m.setAccessible(true);
                                m.invoke(classLoader, file.toURI().toURL());
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        Reflections reflection = new Reflections(OurClassLoader.instance);
        for(Class<? extends Annotation> c : handlers.keySet())
        {
            Set<Class<?>> list = reflection.getTypesAnnotatedWith(c);
            Iterator<Class<?>> it = list.iterator();
            while(it.hasNext())
            {
                Class<?> clazz = it.next();
                try
                {
                    loadAddon(clazz);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        try
        {
            ClassPath path = ClassPath.from(OurClassLoader.instance);
            if(excluded == null || excluded.isEmpty())
                for(ResourceInfo info : path.getResources())
                {
                    if(info.getResourceName().endsWith("/luaAddon.json"))
                    {
                        Log.message("Loading lua addon " + info.getResourceName());
                        addLuaScript(IOUtils.readString(info.url().openStream(), "UTF-8"), game.getAssetsLoader());
                    }
                }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void addLuaScript(String jsonAddonData, ResourceLoader loader) throws UnsupportedEncodingException, IOException
    {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonAddonData, JsonObject.class);
        if(jsonObject.has("id") && jsonObject.has("name") && jsonObject.has("version") && jsonObject.has("mainClass"))
        {
            String id = jsonObject.get("id").getAsString();
            String name = jsonObject.get("name").getAsString();
            String version = jsonObject.get("version").getAsString();
            JsonElement mainClass = jsonObject.get("mainClass");
            String mainClassPath = null;
            if(mainClass.isJsonNull())
            {
                mainClassPath = id + "/main.lua";
            }
            else
                mainClassPath = mainClass.getAsString();

            String author = "unknown";
            if(jsonObject.has("author"))
            {
                author = jsonObject.get("author").getAsString();
            }

            Log.message("Found lua addon with id: " + id + ", name: " + name + ", version: " + version + " and mainClass: " + mainClassPath + ". Author is " + author);

            LuaAddonContainer container = new LuaAddonContainer(id, name, version, author, mainClassPath);
            CommonHandler.setCurrentContainer(container);
            new LuaScript(loader.getResource(new ResourceLocation(mainClassPath)), luaListener, container, game);

            eventBus.register(container);
            File configFolder = new File(SystemUtils.getGameFolder(), "configs/");
            if(!configFolder.exists())
            {
                configFolder.mkdirs();
            }
            Logger logger = LoggerFactory.getLogger(name);
            ModPreInitEvent preInitEvent = new ModPreInitEvent(game, logger, new File(configFolder, container.getId() + ".cfg"), configFolder);
            eventBus.fireEvent(preInitEvent, container, null);
            containers.add(container);
        }
        else
        {
            Log.error("Missing data when loading lua addon: luaAddon.json must contain fields \"id\", \"name\", \"version\" and \"mainClass\"");
        }

    }

    public void exclude(Class<? extends Annotation>... classesToSearch)
    {
        if(classesToSearch == null || classesToSearch.length == 0)
        {
            excluded = null;
        }
        excluded = Lists.newArrayList(classesToSearch);
    }

    public Object getAddonInstance(String addonID)
    {
        return addonInstances.get(addonID);
    }

    public Collection<AddonContainer> getContainersList()
    {
        return containers;
    }
}
