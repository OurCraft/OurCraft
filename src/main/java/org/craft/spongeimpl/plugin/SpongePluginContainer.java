package org.craft.spongeimpl.plugin;

import org.spongepowered.api.plugin.*;

public class SpongePluginContainer implements PluginContainer
{

    private Object plugin;
    private Plugin pluginAnnot;

    public SpongePluginContainer(Object instance, Plugin plugin)
    {
        this.plugin = instance;
        this.pluginAnnot = plugin;
    }

    @Override
    public String getId()
    {
        return pluginAnnot.id();
    }

    @Override
    public String getName()
    {
        return pluginAnnot.name();
    }

    @Override
    public String getVersion()
    {
        return pluginAnnot.version();
    }

    @Override
    public Object getInstance()
    {
        return plugin;
    }

}
