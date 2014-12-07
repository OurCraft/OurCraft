package org.craft.spoonge;

import java.io.*;

import org.craft.*;
import org.craft.modding.*;
import org.craft.spoonge.events.state.*;
import org.craft.spoonge.plugin.*;
import org.craft.utils.*;
import org.slf4j.*;
import org.spongepowered.api.plugin.*;

public class SpoongeAddonHandler implements IAddonHandler<Plugin>
{

    @Override
    public AddonContainer<Plugin> createContainer(Plugin annot, Object object)
    {
        return new SpoongePluginContainer(object, annot);
    }

    @Override
    public void onCreation(OurCraftInstance instance, AddonContainer<Plugin> container)
    {
        File configFolder = new File(SystemUtils.getGameFolder(), "configs/");
        if(!configFolder.exists())
        {
            configFolder.mkdirs();
        }
        AddonData data = new AddonData(container);
        container.setAddonData(data);
        Logger logger = LoggerFactory.getLogger(container.getName());
        SpoongePreInitEvent preInitEvent = new SpoongePreInitEvent(instance, logger, new File(configFolder, container.getId() + ".cfg"), configFolder, configFolder);
        instance.getEventBus().fireEvent(preInitEvent, container.getInstance(), null);
    }

}
