package org.craft.spongeimpl.plugin;

import org.craft.modding.*;
import org.spongepowered.api.plugin.*;

public class SpongePluginContainer extends AddonContainer<Plugin> implements PluginContainer
{

    private String id;
    private String name;
    private String version;

    public SpongePluginContainer(Object instance, Plugin plugin)
    {
        super(plugin, instance);
        try
        {
            id = plugin.id();
            name = plugin.name();
            version = plugin.version();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getVersion()
    {
        return version;
    }

}
