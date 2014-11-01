package org.craft.spongeimpl.tests;

import org.apache.logging.log4j.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.event.world.*;
import org.spongepowered.api.plugin.*;

@Plugin(id = "plugtest", name = "Test plugin")
public class SpongeTestPlugin
{

    private Logger logger;

    public SpongeTestPlugin()
    {

    }

    @SpongeEventHandler
    public void onPreInit(PreInitializationEvent evt)
    {
        logger = evt.getPluginLog();
        evt.getPluginLog().debug("SpoongePreInit!!!");
    }

    @SpongeEventHandler
    public void onPostInit(PostInitializationEvent evt)
    {
        logger.debug("SpoongePostInit!!!");
    }

    @SpongeEventHandler
    public void onWorldLoad(WorldLoadEvent evt)
    {
        logger.debug("SpoongeLoading world " + evt.getWorld().getName());
    }

    @SpongeEventHandler
    public void onWorldUnload(WorldUnloadEvent evt)
    {
        logger.debug("SpoongeUnloading world " + evt.getWorld().getName());
    }

    @SpongeEventHandler
    public void onInit(InitializationEvent evt)
    {
        logger.debug("SpoongeInit!!!");
    }
}
