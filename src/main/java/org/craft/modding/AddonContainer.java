package org.craft.modding;

import java.lang.annotation.*;

public abstract class AddonContainer
{

    private Annotation addonAnnot;
    private Object     addonInstance;

    public AddonContainer(Annotation addonAnnot, Object instance)
    {
        this.addonAnnot = addonAnnot;
        this.addonInstance = instance;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getVersion();

    public Annotation getAddonAnnotation()
    {
        return addonAnnot;
    }

    public Object getInstance()
    {
        return addonInstance;
    }

}
