package org.craft.modding;

import java.lang.annotation.*;

public abstract class AddonContainer<T extends Annotation>
{

    private T      addonAnnot;
    private Object addonInstance;

    public AddonContainer(T addonAnnot, Object instance)
    {
        this.addonAnnot = addonAnnot;
        this.addonInstance = instance;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getVersion();

    public T getAddonAnnotation()
    {
        return addonAnnot;
    }

    public Object getInstance()
    {
        return addonInstance;
    }

}
