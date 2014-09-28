package org.craft.modding.test;

import org.apache.logging.log4j.*;
import org.craft.modding.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.event.world.*;

@Mod(id = "modtest", name = "Test mod", version = "1.0")
public class ModTest
{

    private Logger logger;

    public ModTest()
    {
    }

    @SpongeEventHandler
    public void onPreInit(PreInitializationEvent evt)
    {
        logger = evt.getPluginLog();
        evt.getPluginLog().debug("PreInit!!!");
    }

    @SpongeEventHandler
    public void onPostInit(PostInitializationEvent evt)
    {
        logger.debug("PostInit!!!");
    }

    @SpongeEventHandler
    public void onWorldLoad(WorldLoadEvent evt)
    {
        logger.debug("Loading world " + evt.getWorld().getName());
    }

    @SpongeEventHandler
    public void onWorldUnload(WorldUnloadEvent evt)
    {
        logger.debug("Unloading world " + evt.getWorld().getName());
    }

    @SpongeEventHandler
    public void onInit(InitializationEvent evt)
    {
        logger.debug("Init!!!");
    }
}
