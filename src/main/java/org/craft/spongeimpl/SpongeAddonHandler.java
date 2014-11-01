package org.craft.spongeimpl;

import java.io.*;
import java.lang.annotation.*;

import org.apache.logging.log4j.*;
import org.craft.*;
import org.craft.modding.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.utils.*;

public class SpongeAddonHandler implements IAddonHandler
{

    @Override
    public AddonContainer createContainer(Annotation annot, Object object)
    {
        return new SpongePluginContainer(object, annot);
    }

    @Override
    public void onCreation(OurCraftInstance instance, AddonContainer container)
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
