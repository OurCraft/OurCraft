package org.craft.spongeimpl.plugin;

import org.craft.modding.*;
import org.spongepowered.api.plugin.*;

public class SpongePluginContainer extends AddonContainer implements PluginContainer
{

    private Plugin pluginAnnot;

    public SpongePluginContainer(Object instance, Plugin plugin)
    {
        super(plugin, instance);
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

}
