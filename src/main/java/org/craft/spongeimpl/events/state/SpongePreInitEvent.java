package org.craft.spongeimpl.events.state;

import java.io.*;

import org.craft.*;
import org.slf4j.*;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.util.event.callback.*;

public class SpongePreInitEvent extends SpongeStateEvent implements PreInitializationEvent
{

    private Logger pluginLogger;
    private File   configFile;
    private File   suggestedConfigDir;
    private File   configDir;

    public SpongePreInitEvent(OurCraftInstance game, Logger pluginLogger, File configFile, File suggestedConfigDir, File configDir)
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

    @Override
    public Logger getPluginLog()
    {
        return pluginLogger;
    }

    @Override
    public File getRecommendedConfigurationFile()
    {
        return configFile;
    }

    @Override
    public File getRecommendedConfigurationDirectory()
    {
        return suggestedConfigDir;
    }

    @Override
    public File getConfigurationDirectory()
    {
        return configDir;
    }

    @Override
    public CallbackList getCallbacks()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
