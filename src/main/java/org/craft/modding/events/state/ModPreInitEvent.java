package org.craft.modding.events.state;

import java.io.*;

import org.craft.*;
import org.craft.modding.*;
import org.craft.modding.events.*;
import org.slf4j.*;

public class ModPreInitEvent extends ModEvent
{

    private Logger              logger;
    private File                configFile;
    private File                configFolder;
    private AddonContainer<Mod> container;

    public ModPreInitEvent(OurCraftInstance gameInstance, AddonContainer<Mod> container, Logger logger, File configFile, File configFolder)
    {
        super(gameInstance);
        this.container = container;
        this.logger = logger;
        this.configFile = configFile;
        this.configFolder = configFolder;
    }

    public AddonContainer<Mod> getContainer()
    {
        return container;
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

    public Logger getLogger()
    {
        return logger;
    }

    public File getSuggestedConfigurationFile()
    {
        return configFile;
    }

    public File getSuggestedConfigurationDirectory()
    {
        return configFolder;
    }

    public File getConfigurationDirectory()
    {
        return configFolder;
    }

}
