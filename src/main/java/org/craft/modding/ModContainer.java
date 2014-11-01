package org.craft.modding;

import java.lang.annotation.*;

public class ModContainer extends AddonContainer
{

    private String id;
    private String name;
    private String version;

    public ModContainer(Object instance, Annotation addonAnnot)
    {
        super(addonAnnot, instance);
        try
        {
            id = (String) addonAnnot.annotationType().getDeclaredMethod("id").invoke(addonAnnot);
            name = (String) addonAnnot.annotationType().getDeclaredMethod("name").invoke(addonAnnot);
            version = (String) addonAnnot.annotationType().getDeclaredMethod("version").invoke(addonAnnot);
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
