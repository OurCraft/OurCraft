package org.craft.spongeimpl.tests;

import org.apache.logging.log4j.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.plugin.*;

@Plugin(id = "test", name = "Test")
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
        evt.getPluginLog().debug("Init!!!");
    }

    @SpongeEventHandler
    public void onPostInit(PostInitializationEvent evt)
    {
        logger.debug("PostInit!!!");
    }

    @SpongeEventHandler
    public void onInit(InitializationEvent evt)
    {
        logger.debug("Init!!!");
    }
}
