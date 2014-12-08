package org.craft.modding;

import java.io.*;

import org.craft.*;
import org.craft.modding.events.*;
import org.craft.modding.events.state.*;
import org.craft.utils.*;
import org.slf4j.*;

public class ModHandler implements IAddonHandler<Mod>
{

    @Override
    public AddonContainer<Mod> createContainer(Mod annot, Object object)
    {
        return new ModContainer(object, annot);
    }

    @Override
    public void onCreation(OurCraftInstance instance, AddonContainer<Mod> container)
    {
        File configFolder = new File(SystemUtils.getGameFolder(), "configs/");
        if(!configFolder.exists())
        {
            configFolder.mkdirs();
        }
        Logger logger = LoggerFactory.getLogger(container.getName());
        AddonData addonData = new AddonData(container);
        container.setAddonData(addonData);
        ModPreInitEvent preInitEvent = new ModPreInitEvent(instance, container, logger, new File(configFolder, container.getId() + ".cfg"), configFolder);
        instance.getEventBus().fireEvent(preInitEvent, container.getInstance(), null);
    }

}
