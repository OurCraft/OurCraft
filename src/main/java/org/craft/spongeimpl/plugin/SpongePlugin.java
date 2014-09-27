package org.craft.spongeimpl.plugin;

import org.spongepowered.api.plugin.*;

public class SpongePlugin implements PluginContainer
{

    private Plugin plugin;

    public SpongePlugin(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public String getId()
    {
        return plugin.id();
    }

    @Override
    public String getName()
    {
        return plugin.name();
    }

    @Override
    public String getVersion()
    {
        return plugin.version();
    }

    @Override
    public Object getInstance()
    {
        return plugin;
    }

}
