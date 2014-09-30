package org.craft.modding;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import org.apache.logging.log4j.*;
import org.craft.spongeimpl.events.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.utils.*;
import org.reflections.*;
import org.spongepowered.api.*;

public class AddonsLoader
{

    private File                                                   addonsFolder;
    private HashMap<Class<? extends Annotation>, IAddonManager<?>> handlers;
    private EventBus                                               eventBus;
    private Game                                                   game;

    public AddonsLoader(Game gameInstance, File addonsFolder, EventBus eventBus)
    {
        this.game = gameInstance;
        this.eventBus = eventBus;
        this.addonsFolder = addonsFolder;
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
        for(Class<? extends Annotation> c : handlers.keySet())
        {
            Log.message("checking " + c);
            if(clazz.isAnnotationPresent(c))
            {
                IAddonManager manager = handlers.get(c);
                IAddonHandler handler = manager.getHandler();
                Object instance = clazz.newInstance();
                AddonContainer container = handler.createContainer(clazz.getAnnotation(c), instance);
                manager.loadAddon(container);
                eventBus.register(instance);

                File configFolder = new File(SystemUtils.getGameFolder(), "configs/");
                if(!configFolder.exists())
                {
                    configFolder.mkdirs();
                }
                Logger logger = new AddonLogger(container);
                SpongePreInitEvent preInitEvent = new SpongePreInitEvent(game, logger, new File(configFolder, container.getId() + ".cfg"), configFolder, configFolder);
                eventBus.fireEvent(preInitEvent, instance);
                added = true;
            }
        }
        if(!added)
        {
            Log.error("Tried to register addon " + clazz.getCanonicalName() + " but it is not supported");
        }
    }

    public File getAddonsFolder()
    {
        return addonsFolder;
    }

    public void loadAll(Reflections reflection, File... folders)
    {
        for(File folder : folders)
        {
            File[] files = folder.listFiles();
            for(File file : files)
            {
                if(file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip"))
                {
                    tryToAddToClassPath(file);
                }
            }
        }

        for(Class<? extends Annotation> c : handlers.keySet())
        {
            for(Class<?> clazz : reflection.getTypesAnnotatedWith(c))
            {
                try
                {
                    loadAddon(clazz);
                }
                catch(InstantiationException e)
                {
                    e.printStackTrace();
                }
                catch(IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean tryToAddToClassPath(URL path)
    {
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        try
        {
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class<?>[]
            {
                    URL.class
            });
            addURL.setAccessible(true);
            addURL.invoke(classLoader, new Object[]
            {
                    path
            });
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private boolean tryToAddToClassPath(File f)
    {
        try
        {
            return tryToAddToClassPath(f.toURI().toURL());
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
