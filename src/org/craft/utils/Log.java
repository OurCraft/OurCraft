package org.craft.utils;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;

public class Log
{

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface NonLoggable
    {
    }

    public static boolean showCaller = true;

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
                if(elem.getClassName().contains(Log.class.getCanonicalName())) continue;
                try
                {
                    Class<?> c = Class.forName(elem.getClassName());
                    if(c.isAnnotationPresent(NonLoggable.class)) continue;
                    for(Method method : c.getDeclaredMethods())
                    {
                        if(method.getName().equals(elem.getMethodName())) if(method.isAnnotationPresent(NonLoggable.class)) continue elementsIteration;
                    }
                    String s = elem.getClassName() + "." + elem.getMethodName() + ":" + elem.getLineNumber();
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
        log("[INFO] " + msg, System.out, format);
    }

    @NonLoggable
    public static void error(String msg)
    {
        error(msg, true);
    }

    @NonLoggable
    public static void error(String msg, boolean format)
    {
        log("[ERROR] " + msg, System.err, format);
    }

    @NonLoggable
    private static void log(String msg, PrintStream out, boolean format)
    {
        String finalMessage = msg;
        // TODO format
        out.println(finalMessage);
    }

    @NonLoggable
    public static void fatal(String string)
    {
        System.err.println(string);
        System.exit(-1); // TODO: needs crash report'n' stuff
    }
}
