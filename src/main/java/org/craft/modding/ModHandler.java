package org.craft.modding;

public class ModHandler implements IAddonHandler<Mod>
{

    @Override
    public AddonContainer createContainer(Mod annot, Object object)
    {
        return new ModContainer(object, annot);
    }

}
