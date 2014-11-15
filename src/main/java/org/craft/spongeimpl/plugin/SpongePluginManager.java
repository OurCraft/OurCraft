package org.craft.spongeimpl.plugin;

import java.util.*;

import com.google.common.base.Optional;

import org.craft.modding.*;
import org.craft.spongeimpl.*;
import org.slf4j.*;
import org.spongepowered.api.plugin.*;

public class SpongePluginManager implements IAddonManager<Plugin>, PluginManager
{

    private HashMap<String, PluginContainer> plugins;
    private HashMap<PluginContainer, Logger> loggers;
    private SpongeAddonHandler               handler;

    public SpongePluginManager()
    {
        plugins = new HashMap<String, PluginContainer>();
        loggers = new HashMap<PluginContainer, Logger>();
        this.handler = new SpongeAddonHandler();
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

}
