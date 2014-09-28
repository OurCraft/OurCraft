package org.craft.spongeimpl.plugin;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.message.*;
import org.apache.logging.log4j.spi.*;
import org.craft.utils.*;
import org.craft.utils.Log.NonLoggable;
import org.spongepowered.api.plugin.*;

public class PluginLogger extends AbstractLogger
{

    private Plugin annot;

    public PluginLogger(Plugin annot)
    {
        this.annot = annot;
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, Message data, Throwable t)
    {
        return true;
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, Object data, Throwable t)
    {
        return true;
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String data)
    {
        return true;
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String data, Object... p1)
    {
        return true;
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String data, Throwable t)
    {
        return true;
    }

    @Override
    @NonLoggable
    public void log(Marker marker, String fqcn, Level level, Message data, Throwable t)
    {
        Log.log("[" + annot.name() + "] " + data.getFormattedMessage(), toLog(level), false);
        if(t != null)
            t.printStackTrace();
    }

    private java.util.logging.Level toLog(Level level)
    {
        switch(level)
        {
            case DEBUG:
                return java.util.logging.Level.CONFIG;
            case ERROR:
                return java.util.logging.Level.SEVERE;
            case WARN:
                return java.util.logging.Level.WARNING;
            case FATAL:
                return java.util.logging.Level.SEVERE;
            case ALL:
                return java.util.logging.Level.ALL;
            case INFO:
                return java.util.logging.Level.INFO;
            case TRACE:
                return java.util.logging.Level.FINEST;
            case OFF:
                return java.util.logging.Level.OFF;
            default:
                return java.util.logging.Level.INFO;
        }
    }

}
