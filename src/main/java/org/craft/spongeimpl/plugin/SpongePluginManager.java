package org.craft.spongeimpl.plugin;

import java.util.*;

import org.apache.logging.log4j.*;
import org.craft.modding.*;
import org.craft.spongeimpl.*;
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
    public PluginContainer getPlugin(String id)
    {
        return plugins.get(id);
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

    public void loadAddon(AddonContainer container)
    {
        plugins.put(container.getId(), (PluginContainer) container);
    }

    @Override
    public AddonContainer getAddon(String id)
    {
        return (AddonContainer) getPlugin(id);
    }

    @Override
    public Collection<AddonContainer> getAddons()
    {
        ArrayList<AddonContainer> containers = new ArrayList<AddonContainer>();
        for(PluginContainer plugin : getPlugins())
        {
            if(plugin instanceof AddonContainer)
            {
                containers.add((AddonContainer) plugin);
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
