package org.craft.spoonge.plugin;

import java.lang.reflect.Constructor;
import java.util.*;

import com.google.common.base.Optional;

import org.craft.modding.*;
import org.craft.spoonge.*;
import org.slf4j.*;
import org.spongepowered.api.plugin.*;

public class SpoongePluginManager implements IAddonManager<Plugin>, PluginManager
{

    private HashMap<String, PluginContainer> plugins;
    private HashMap<PluginContainer, Logger> loggers;
    private SpoongeAddonHandler              handler;

    public SpoongePluginManager()
    {
        plugins = new HashMap<String, PluginContainer>();
        loggers = new HashMap<PluginContainer, Logger>();
        this.handler = new SpoongeAddonHandler();
    }

    @Override
    public Optional<PluginContainer> getPlugin(String id)
    {
        return Optional.of(plugins.get(id));
    }

    @Override
    public Logger getLogger(PluginContainer plugin)
    {
        return loggers.get(plugin);
    }

    @Override
    public Collection<PluginContainer> getPlugins()
    {
        return plugins.values();
    }

    @Override
    public void loadAddon(AddonContainer<Plugin> container)
    {
        plugins.put(container.getId(), (PluginContainer) container);
    }

    @SuppressWarnings("unchecked")
    @Override
    public AddonContainer<Plugin> getAddon(String id)
    {
        return (AddonContainer<Plugin>) getPlugin(id).get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<AddonContainer<Plugin>> getAddons()
    {
        ArrayList<AddonContainer<Plugin>> containers = new ArrayList<AddonContainer<Plugin>>();
        for(PluginContainer plugin : getPlugins())
        {
            if(plugin instanceof AddonContainer)
            {
                containers.add((AddonContainer<Plugin>) plugin);
            }
        }
        return containers;
    }

    @Override
    public IAddonHandler<Plugin> getHandler()
    {
        return handler;
    }

    @Override public Constructor getAddonConstructor(Class<?> clazz)
    {
        return clazz.getConstructors()[0];
    }

    @Override public Object[] getConstructorArgs()
    {
        return new Object[0];
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance)
    {
        for(PluginContainer container : plugins.values())
        {
            if(container.getInstance() == instance)
                return Optional.of(container);
        }
        return Optional.absent();
    }

    @Override
    public boolean isLoaded(String id)
    {
        // TODO Auto-generated method stub
        return false;
    }

}
