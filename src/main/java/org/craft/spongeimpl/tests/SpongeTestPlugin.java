package org.craft.spongeimpl.tests;

import org.slf4j.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.event.world.*;
import org.spongepowered.api.plugin.*;

@Plugin(id = "plugtest", name = "Test plugin", version = "1.0")
public class SpongeTestPlugin
{

    private Logger logger;

    public SpongeTestPlugin()
    {

    }

    @Subscribe
    public void onPreInit(PreInitializationEvent evt)
    {
        logger = evt.getPluginLog();
        evt.getPluginLog().debug("SpongePreInit!!!");
    }

    @Subscribe
    public void onPostInit(PostInitializationEvent evt)
    {
        logger.debug("SpongePostInit!!!");
        logger.debug("Dirt is " + evt.getGame().getRegistry().getBlock("ourcraft:dirt").get().getId());
    }

    @Subscribe
    public void onWorldLoad(WorldLoadEvent evt)
    {
        logger.debug("SpongeLoading world " + evt.getWorld().getName());
    }

    @Subscribe
    public void onWorldUnload(WorldUnloadEvent evt)
    {
        logger.debug("SpongeUnloading world " + evt.getWorld().getName());
    }

    @Subscribe
    public void onInit(InitializationEvent evt)
    {
        logger.debug("SpongeInit!!!");
    }
}
