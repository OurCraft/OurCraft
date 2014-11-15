package org.craft.modding.events;

import java.io.*;

import org.craft.*;
import org.slf4j.*;

public class ModPreInitEvent extends ModEvent
{

    private Logger logger;
    private File   configFile;
    private File   configFolder;

    public ModPreInitEvent(OurCraftInstance gameInstance, Logger logger, File configFile, File configFolder)
    {
        super(gameInstance);
        this.logger = logger;
        this.configFile = configFile;
        this.configFolder = configFolder;
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
