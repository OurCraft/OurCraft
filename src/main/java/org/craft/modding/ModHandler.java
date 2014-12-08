package org.craft.modding;

import java.io.*;

import com.google.gson.*;

import org.craft.*;
import org.craft.modding.events.state.*;
import org.craft.resources.*;
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
        ResourceLocation metaFileLoc = new ResourceLocation(container.getId(), "addoninfos.json");
        if(instance.getAssetsLoader().doesResourceExists(metaFileLoc))
        {
            try
            {
                AbstractResource res = instance.getAssetsLoader().getResource(metaFileLoc);
                String content = new String(res.getData(), "UTF-8");
                Gson gson = new Gson();
                JsonObject root = gson.fromJson(content, JsonObject.class);
                if(root.has("description"))
                {
                    addonData.setDescription(root.get("description").getAsString());
                }
                else if(root.has("desc"))
                {
                    addonData.setDescription(root.get("desc").getAsString());
                }
                if(root.has("logoPath"))
                {
                    String path = root.get("logoPath").getAsString();
                    if(!path.contains(":"))
                        addonData.setLogoPath(new ResourceLocation(container.getId(), path));
                    else
                        addonData.setLogoPath(new ResourceLocation(path));
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        ModPreInitEvent preInitEvent = new ModPreInitEvent(instance, container, logger, new File(configFolder, container.getId() + ".cfg"), configFolder);
        instance.getEventBus().fireEvent(preInitEvent, container.getInstance(), null);
    }

}
