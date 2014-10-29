package org.craft.modding.test;

import org.apache.logging.log4j.*;
import org.craft.modding.*;
import org.craft.modding.events.*;

@Mod(id = "modtest", name = "Test mod", version = "1.0")
public class ModTest
{

    private Logger logger;

    public ModTest()
    {
    }

    @OurModEventHandler
    public void onPreInit(ModPreInitEvent evt)
    {
        logger = evt.getLogger();
        logger.debug("PreInit!!!");
    }

    @OurModEventHandler
    public void onPostInit(ModPostInitEvent evt)
    {
        logger.debug("PostInit!!!");
    }

    @OurModEventHandler
    public void onWorldLoad(WorldLoadEvent evt)
    {
        logger.debug("Loading world " + evt.getWorld().getName());
    }

    @OurModEventHandler
    public void onWorldUnload(WorldUnloadEvent evt)
    {
        logger.debug("Unloading world " + evt.getWorld().getName());
    }

    @OurModEventHandler
    public void onInit(ModInitEvent evt)
    {
        logger.debug("Init!!!");
    }
}
