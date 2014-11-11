package org.craft.spongeimpl;

import java.io.*;

import org.apache.logging.log4j.*;
import org.craft.*;
import org.craft.modding.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.utils.*;
import org.spongepowered.api.plugin.*;

public class SpongeAddonHandler implements IAddonHandler<Plugin>
{

    @Override
    public AddonContainer<Plugin> createContainer(Plugin annot, Object object)
    {
        return new SpongePluginContainer(object, annot);
    }

    @Override
    public void onCreation(OurCraftInstance instance, AddonContainer<Plugin> container)
    {
        File configFolder = new File(SystemUtils.getGameFolder(), "configs/");
        if(!configFolder.exists())
        {
            configFolder.mkdirs();
        }
        Logger logger = new AddonLogger(container);
        SpongePreInitEvent preInitEvent = new SpongePreInitEvent(instance, logger, new File(configFolder, container.getId() + ".cfg"), configFolder, configFolder);
        instance.getEventBus().fireEvent(preInitEvent, container.getInstance(), null);
    }

}
