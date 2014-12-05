package org.craft.spoonge.events.state;

import java.io.*;

import org.craft.*;
import org.slf4j.*;
import org.spongepowered.api.event.state.*;

public class SpoongePreInitEvent extends SpoongeStateEvent implements PreInitializationEvent
{

    private Logger pluginLogger;
    private File   configFile;
    private File   suggestedConfigDir;
    private File   configDir;

    public SpoongePreInitEvent(OurCraftInstance game, Logger pluginLogger, File configFile, File suggestedConfigDir, File configDir)
    {
        super(game);
        this.pluginLogger = pluginLogger;
        this.configFile = configFile;
        this.suggestedConfigDir = suggestedConfigDir;
        this.configDir = configDir;
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }


}
