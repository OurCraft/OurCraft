package org.craft.spongeimpl.plugin;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.*;
import org.craft.spongeimpl.events.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.utils.*;
import org.spongepowered.api.*;
import org.spongepowered.api.plugin.*;

public class SpongePluginManager implements PluginManager
{

    private HashMap<String, PluginContainer> plugins;
    private EventBus                         eventBus;
    private Game                             game;

    public SpongePluginManager(Game gameInstance, EventBus eventBus)
    {
        this.game = gameInstance;
        plugins = new HashMap<String, PluginContainer>();
        this.eventBus = eventBus;
    }

    @Override
    public PluginContainer getPlugin(String id)
    {
        return plugins.get(id);
    }

    @Override
    public Logger getLogger(PluginContainer plugin)
    {
        // TODO
        return null;
    }

    @Override
    public Collection<PluginContainer> getPlugins()
    {
        return plugins.values();
    }

    public void loadPlugin(PluginContainer plugin)
    {
        plugins.put(plugin.getId(), plugin);
    }

    public void loadPlugin(Class<?> clazz)
    {
        if(clazz.isAnnotationPresent(Plugin.class))
        {
            Plugin annot = clazz.getAnnotation(Plugin.class);
            Object instance;
            try
            {
                instance = clazz.newInstance();
                plugins.put(annot.id(), new SpongePluginContainer(instance, annot));
                eventBus.register(instance);
                File configFolder = new File(SystemUtils.getGameFolder(), "configs/");
                if(!configFolder.exists())
                {
                    configFolder.mkdirs();
                }
                Logger logger = new PluginLogger(annot);
                SpongePreInitEvent preInitEvent = new SpongePreInitEvent(game, logger, new File(configFolder, annot.id() + ".cfg"), configFolder, configFolder);
                eventBus.fireEvent(preInitEvent, instance);
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
        else
        {
            Log.error("Class " + clazz.getCanonicalName() + " was marked to be registred as plugin when it does not have a Plugin annotation");
        }
    }
}
