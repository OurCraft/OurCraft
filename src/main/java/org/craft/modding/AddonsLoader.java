package org.craft.modding;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;
import com.google.gson.*;

import org.apache.logging.log4j.*;
import org.craft.*;
import org.craft.modding.events.*;
import org.craft.modding.script.lua.*;
import org.craft.resources.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.utils.*;
import org.reflections.*;

public class AddonsLoader
{

    private HashMap<Class<? extends Annotation>, IAddonManager<?>> handlers;
    private EventBus                                               eventBus;
    private OurCraftInstance                                       game;
    private LuaEventBusListener                                    luaListener;
    private OurClassLoader                                         classLoader;
    private ArrayList<AddonContainer>                              containers;
    private ArrayList<Class<?>>                                    loaded;

    public AddonsLoader(OurCraftInstance gameInstance, EventBus eventBus)
    {
        containers = Lists.newArrayList();
        loaded = Lists.newArrayList();
        this.classLoader = (OurClassLoader) Thread.currentThread().getContextClassLoader();
        luaListener = new LuaEventBusListener();
        eventBus.addListener(luaListener);

        this.game = gameInstance;
        this.eventBus = eventBus;
        handlers = new HashMap<Class<? extends Annotation>, IAddonManager<?>>();
        registerAddonAnnotation(Mod.class, new ModManager());

    }

    @SuppressWarnings("rawtypes")
    public void registerAddonAnnotation(Class<? extends Annotation> annot, IAddonManager handler)
    {
        handlers.put(annot, handler);
    }

    @SuppressWarnings(
    {
            "rawtypes", "unchecked"
    })
    public void loadAddon(Class<?> clazz) throws InstantiationException, IllegalAccessException
    {
        boolean added = false;
        if(loaded.contains(clazz))
            return;
        annotLoop: for(Class<? extends Annotation> c : handlers.keySet())
        {
            for(AddonContainer container : containers)
            {
                if(container.getAddonAnnotation().annotationType() == c && container.getInstance().getClass() == clazz)
                {
                    added = true;
                    continue annotLoop;
                }
            }
            if(clazz.isAnnotationPresent(c))
            {
                loaded.add(clazz);
                IAddonManager manager = handlers.get(c);
                IAddonHandler handler = manager.getHandler();
                Object instance = clazz.newInstance();
                for(Field f : clazz.getDeclaredFields())
                {
                    if(f.isAnnotationPresent(Instance.class))
                    {
                        f.setAccessible(true);
                        f.set(null, instance);
                    }
                }
                AddonContainer container = handler.createContainer(clazz.getAnnotation(c), instance);
                manager.loadAddon(container);
                eventBus.register(instance);

                added = true;
                Log.message("Loaded addon \"" + container.getName() + "\" as " + c.getSimpleName());
                containers.add(container);
                handler.onCreation(game, container);
            }
        }
        if(!added)
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
                                new LuaScript(zipResLoader.getResource(new ResourceLocation(mainClass)), luaListener, container, game);

                                eventBus.register(container);
                                File configFolder = new File(SystemUtils.getGameFolder(), "configs/");
                                if(!configFolder.exists())
                                {
                                    configFolder.mkdirs();
                                }
                                Logger logger = new AddonLogger(container);
                                ModPreInitEvent preInitEvent = new ModPreInitEvent(game, logger, new File(configFolder, container.getId() + ".cfg"), configFolder);
                                eventBus.fireEvent(preInitEvent, container, null);
                                containers.add(container);
                            }
                            else
                            {
                                Log.error("Missing data when loading lua addon: luaAddon.json must contain fields \"id\", \"name\", \"version\" and \"mainClass\"");
                            }

                            this.classLoader.addFile(file);
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        Reflections reflection = new Reflections();
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
    }

}
