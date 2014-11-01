package org.craft.modding;

import java.io.*;
import java.lang.annotation.*;

import org.apache.logging.log4j.*;
import org.craft.*;
import org.craft.modding.events.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.utils.*;

public class ModHandler implements IAddonHandler
{

    @Override
    public AddonContainer createContainer(Annotation annot, Object object)
    {
        return new ModContainer(object, annot);
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
        ModPreInitEvent preInitEvent = new ModPreInitEvent(instance, logger, new File(configFolder, container.getId() + ".cfg"), configFolder);
        instance.getEventBus().fireEvent(preInitEvent, container.getInstance(), null);
    }

}
