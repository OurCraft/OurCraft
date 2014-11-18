package org.craft.utils;

import java.io.PrintStream;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.logging.*;

import org.craft.client.*;
import org.craft.utils.crash.*;

public class Log
{
    private static final Logger log = Logger.getLogger("OurCraft");
    private static LogFormater  logformatter;
    static
    {
        LogManager.getLogManager().reset();
        logformatter = new LogFormater();
        Handler[] ahandler = log.getHandlers();
        int i = ahandler.length;

        for(int j = 0; j < i; ++j)
        {
            Handler handler = ahandler[j];
            log.removeHandler(handler);
        }
        ConsoleHandler ch = new ConsoleHandler();

        ch.setFormatter(logformatter);
        log.addHandler(ch);
        ch.setLevel(Level.ALL);
        log.setLevel(Level.ALL);                         
                                                     
        LoggingOutputStream los;                                               
                      
        los = new LoggingOutputStream(log, Level.INFO);          
        System.setOut(new PrintStream(los, true));                             
                               
        los= new LoggingOutputStream(log, Level.SEVERE);           
        System.setErr(new PrintStream(los, true));                             

    }

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
        log(msg, Level.INFO, format);
    }

    @NonLoggable
    public static void error(String msg)
    {
        error(msg, true);
    }

    @NonLoggable
    public static void error(String msg, boolean format)
    {
        log(msg, Level.SEVERE, format);
    }

    @NonLoggable
    public static void log(String msg, Level lvl, boolean format)
    {
        String finalMessage = msg;
        // TODO format
        if(showCaller)
        {
            finalMessage = "[In " + getCaller() + "] " + finalMessage;
        }
        log.log(lvl, finalMessage);
    }

    @NonLoggable
    public static void fatal(String string)
    {
        if(OurCraft.getOurCraft() != null)
            OurCraft.getOurCraft().crash(new CrashReport(string));
        else
        {
            new CrashReport(string).printStack();
            System.exit(-2);
        }
    }

    @NonLoggable
    public static void debug(String string)
    {
        log(string, Level.FINE, true);
    }

    public static void addHandler(Handler h)
    {
        log.addHandler(h);
        h.setFormatter(logformatter);
    }
}
