package org.craft.modding;

public class ModContainer extends AddonContainer
{

    private Mod mod;

    public ModContainer(Object instance, Mod addonAnnot)
    {
        super(addonAnnot, instance);
        this.mod = addonAnnot;
    }

    @Override
    public String getId()
    {
        return mod.id();
    }

    @Override
    public String getName()
    {
        return mod.name();
    }

    @Override
    public String getVersion()
    {
        return mod.version();
    }

}
