package org.craft.spongeimpl.plugin;

import java.util.logging.*;

import org.craft.modding.*;
import org.craft.utils.Log.NonLoggable;
import org.slf4j.*;
import org.slf4j.Logger;

/**
 * Class to reimplement!
 *
 */
public class AddonLogger implements Logger
{

    private AddonContainer annot;

    public AddonLogger(AddonContainer annot)
    {
        this.annot = annot;
    }

    @NonLoggable
    public void log(Marker marker, String fqcn, Level level, String data, Throwable t)
    {
        //        Log.log("[" + annot.getName() + "] " + data.getFormattedMessage(), toLog(level), false);
        if(t != null)
            t.printStackTrace();
    }

    private java.util.logging.Level toLog(Level level)
    {
        /* switch(level)
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
         }*/
        return null;
    }

    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isTraceEnabled()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void trace(String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void trace(String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void trace(String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void trace(String format, Object... arguments)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void trace(String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isTraceEnabled(Marker marker)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void trace(Marker marker, String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void trace(Marker marker, String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void trace(Marker marker, String format, Object... argArray)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void trace(Marker marker, String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDebugEnabled()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void debug(String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(String format, Object... arguments)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDebugEnabled(Marker marker)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void debug(Marker marker, String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(Marker marker, String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(Marker marker, String format, Object... arguments)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(Marker marker, String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isInfoEnabled()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void info(String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(String format, Object... arguments)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isInfoEnabled(Marker marker)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void info(Marker marker, String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Marker marker, String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Marker marker, String format, Object... arguments)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Marker marker, String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isWarnEnabled()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void warn(String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(String format, Object... arguments)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isWarnEnabled(Marker marker)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void warn(Marker marker, String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Marker marker, String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Marker marker, String format, Object... arguments)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Marker marker, String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isErrorEnabled()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void error(String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(String format, Object... arguments)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isErrorEnabled(Marker marker)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void error(Marker marker, String msg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Marker marker, String format, Object arg)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Marker marker, String format, Object... arguments)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Marker marker, String msg, Throwable t)
    {
        // TODO Auto-generated method stub

    }

}
