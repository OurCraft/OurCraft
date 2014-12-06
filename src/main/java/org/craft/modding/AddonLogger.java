package org.craft.modding;

import org.craft.utils.*;
import org.craft.utils.Log.NonLoggable;
import org.slf4j.*;
import org.slf4j.Logger;

@SuppressWarnings("rawtypes")
public class AddonLogger implements Logger
{

    private AddonContainer annot;

    public AddonLogger(AddonContainer annot)
    {
        this.annot = annot;
    }

    @NonLoggable
    public String format(String rawMsg)
    {
        return "[" + annot.getName() + " " + annot.getVersion() + "] " + rawMsg;
    }

    @Override
    public String getName()
    {
        return "AddonLogger for " + annot.getName();
    }

    @Override
    public boolean isTraceEnabled()
    {
        return true;
    }

    @Override
    public void trace(String msg)
    {
        trace(msg, (Throwable) null);
    }

    @Override
    public void trace(String format, Object arg)
    {
        trace(String.format(format, arg));
    }

    @Override
    public void trace(String format, Object arg1, Object arg2)
    {
        trace(String.format(format, arg1, arg2));
    }

    @Override
    public void trace(String format, Object... arguments)
    {
        trace(String.format(format, arguments));
    }

    @Override
    public void trace(String msg, Throwable t)
    {
        Log.trace(format(msg), t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker)
    {
        return true;
    }

    @Override
    public void trace(Marker marker, String msg)
    {
        trace(msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg)
    {
        trace(String.format(format, arg));
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2)
    {
        trace(String.format(format, arg1, arg2));
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray)
    {
        trace(String.format(format, argArray));
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t)
    {
        trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled()
    {
        return true;
    }

    @Override
    public void debug(String msg)
    {
        debug(msg, (Throwable) null);
    }

    @Override
    public void debug(String format, Object arg)
    {
        debug(String.format(format, arg));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2)
    {
        debug(String.format(format, arg1, arg2));
    }

    @Override
    public void debug(String format, Object... arguments)
    {
        debug(String.format(format, arguments));
    }

    @Override
    public void debug(String msg, Throwable t)
    {
        Log.debug(format(msg), t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker)
    {
        return true;
    }

    @Override
    public void debug(Marker marker, String msg)
    {
        debug(msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg)
    {
        debug(String.format(format, arg));
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2)
    {
        debug(String.format(format, arg1, arg2));
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments)
    {
        debug(String.format(format, arguments));
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t)
    {
        debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled()
    {
        return true;
    }

    @Override
    public void info(String msg)
    {
        info(msg, (Throwable) null);
    }

    @Override
    public void info(String format, Object arg)
    {
        info(String.format(format, arg));
    }

    @Override
    public void info(String format, Object arg1, Object arg2)
    {
        info(String.format(format, arg1, arg2));
    }

    @Override
    public void info(String format, Object... arguments)
    {
        info(String.format(format, arguments));
    }

    @Override
    public void info(String msg, Throwable t)
    {
        Log.message(format(msg), t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker)
    {
        return true;
    }

    @Override
    public void info(Marker marker, String msg)
    {
        info(msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg)
    {
        info(String.format(format, arg));
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2)
    {
        info(String.format(format, arg1, arg2));
    }

    @Override
    public void info(Marker marker, String format, Object... arguments)
    {
        info(String.format(format, arguments));
    }

    @Override
    public void info(Marker marker, String msg, Throwable t)
    {
        info(msg, t);
    }

    @Override
    public boolean isWarnEnabled()
    {
        return true;
    }

    @Override
    public void warn(String msg)
    {
        warn(msg, (Throwable) null);
    }

    @Override
    public void warn(String format, Object arg)
    {
        warn(String.format(format, arg));
    }

    @Override
    public void warn(String format, Object... arguments)
    {
        warn(String.format(format, arguments));
    }

    @Override
    public void warn(String format, Object arg1, Object arg2)
    {
        warn(String.format(format, arg1, arg2));
    }

    @Override
    public void warn(String msg, Throwable t)
    {
        warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker)
    {
        return true;
    }

    @Override
    public void warn(Marker marker, String msg)
    {
        warn(msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg)
    {
        warn(String.format(format, arg));
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2)
    {
        warn(String.format(format, arg1, arg2));
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments)
    {
        warn(String.format(format, arguments));
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t)
    {
        warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled()
    {
        return true;
    }

    @Override
    public void error(String msg)
    {
        error(msg, (Throwable) null);
    }

    @Override
    public void error(String format, Object arg)
    {
        error(String.format(format, arg), (Throwable) null);
    }

    @Override
    public void error(String format, Object arg1, Object arg2)
    {
        error(String.format(format, arg1, arg2), (Throwable) null);
    }

    @Override
    public void error(String format, Object... arguments)
    {
        error(String.format(format, arguments), (Throwable) null);
    }

    @Override
    public void error(String msg, Throwable t)
    {
        
        Log.error(format(msg), t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker)
    {
        return true;
    }

    @Override
    public void error(Marker marker, String msg)
    {
        error(String.format(msg), (Throwable) null);
    }

    @Override
    public void error(Marker marker, String format, Object arg)
    {
        error(String.format(format, arg), (Throwable) null);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2)
    {
        error(String.format(format, arg1, arg2), (Throwable) null);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments)
    {
        error(String.format(format, arguments), (Throwable) null);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t)
    {
        error(msg, t);
    }

}
