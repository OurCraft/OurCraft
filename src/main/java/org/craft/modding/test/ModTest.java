package org.craft.modding.test;

import org.apache.logging.log4j.*;
import org.craft.modding.*;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.event.world.*;

@Mod(id = "modtest", name = "Test mod", version = "1.0")
public class ModTest
{

    private Logger logger;

    public ModTest()
    {
    }

    @OurModEventHandler
    public void onPreInit(PreInitializationEvent evt)
    {
        logger = evt.getPluginLog();
        evt.getPluginLog().debug("PreInit!!!");
    }

    @OurModEventHandler
    public void onPostInit(PostInitializationEvent evt)
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
    public void onInit(InitializationEvent evt)
    {
        logger.debug("Init!!!");
    }
}
