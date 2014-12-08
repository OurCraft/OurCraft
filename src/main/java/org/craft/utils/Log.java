package org.craft.utils;

import java.lang.annotation.*;
import java.lang.reflect.*;

import org.craft.client.*;
import org.craft.utils.Log.NonLoggable;
import org.craft.utils.crash.*;
import org.slf4j.*;

@NonLoggable
public final class Log
{
    private static final Logger log = LoggerFactory.getLogger("Ourcraft");

    /**
     * If a method or a class has this annotation on, the logger will ignore them when searching for the caller.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface NonLoggable
    {
    }

    public static boolean showCaller        = true;
    public static boolean useFullClassNames = false;

    @NonLoggable
    public static void message(String msg)
    {
        message(msg, true);
    }

    @NonLoggable
    private static String format(String rawMessage)
    {
        // TODO format
        if(showCaller)
        {
            return "[In " + getCaller() + "] " + rawMessage;
        }
        return rawMessage;
    }

    @NonLoggable
    private static String getCaller()
    {
        StackTraceElement[] elems = Thread.currentThread().getStackTrace();
        if(elems != null)
        {
            elementsIteration: for(int i = 1; i < elems.length; i++ )
            {
                StackTraceElement elem = elems[i];
                if(elem.getClassName().contains(Log.class.getCanonicalName()))
                    continue;
                if(elem.getClassName().contains(org.slf4j.Logger.class.getCanonicalName()))
                    continue;
                if(elem.getClassName().contains(Logger.class.getCanonicalName()))
                    continue;
                try
                {
                    Class<?> c = Class.forName(elem.getClassName());
                    if(c.isAnnotationPresent(NonLoggable.class))
                        continue;
                    for(Method method : c.getDeclaredMethods())
                    {
                        if(method.getName().equals(elem.getMethodName()))
                            if(method.isAnnotationPresent(NonLoggable.class))
                                continue elementsIteration;
                    }

                    String name = null;
                    if(useFullClassNames)
                    {
                        name = c.getCanonicalName();
                    }
                    else
                        name = c.getSimpleName();
                    String s = name + "." + elem.getMethodName() + ":" + elem.getLineNumber();
                    return s;
                }
                catch(ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return "Unknown source";
    }

    @NonLoggable
    public static void message(String msg, boolean format)
    {
        String formated = format(msg);
        log.info(formated);
    }

    @NonLoggable
    public static void error(String msg)
    {
        error(msg, true);
    }

    @NonLoggable
    public static void error(String msg, boolean format)
    {
        String formated = format(msg);
        log.error(formated);
    }

    @NonLoggable
    public static void fatal(String msg)
    {
        if(OurCraft.getOurCraft() != null)
            OurCraft.getOurCraft().crash(new CrashReport(msg));
        else
        {
            new CrashReport(msg).printStack();
            System.exit(-2);
        }
    }

    @NonLoggable
    public static void debug(String msg)
    {
        String formated = format(msg);
        log.debug(formated);
    }

    @NonLoggable
    public static void error(String formated, Throwable t)
    {
        log.error(formated, t);
    }

    @NonLoggable
    public static void debug(String formated, Throwable t)
    {
        log.debug(formated, t);
    }

    @NonLoggable
    public static void message(String formated, Throwable t)
    {
        log.info(formated, t);
    }

    @NonLoggable
    public static void trace(String formated, Throwable t)
    {
        log.trace(formated, t);
    }

}
