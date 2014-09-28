package org.craft.spongeimpl;

import org.craft.modding.*;
import org.craft.spongeimpl.plugin.*;
import org.spongepowered.api.plugin.*;

public class SpongeAddonHandler implements IAddonHandler<Plugin>
{

    @Override
    public AddonContainer createContainer(Plugin annot, Object object)
    {
        return new SpongePluginContainer(object, annot);
    }

}
