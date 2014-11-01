package org.craft.spongeimpl.plugin;

import java.lang.annotation.*;

import org.craft.modding.*;
import org.spongepowered.api.plugin.*;

public class SpongePluginContainer extends AddonContainer implements PluginContainer
{

    private String id;
    private String name;
    private String version;

    public SpongePluginContainer(Object instance, Annotation plugin)
    {
        super(plugin, instance);
        try
        {
            id = (String) plugin.annotationType().getDeclaredMethod("id").invoke(plugin);
            name = (String) plugin.annotationType().getDeclaredMethod("name").invoke(plugin);
            version = (String) plugin.annotationType().getDeclaredMethod("version").invoke(plugin);
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
