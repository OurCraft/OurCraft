package org.craft.utils;

import java.io.*;

public final class Dev
{
    private static boolean debug;
    private static File    debugFolder;

    public static boolean debug()
    {
        return debug;
    }

    public static void debug(boolean debug)
    {
        Dev.debug = debug;
        if(debug && debugFolder == null)
        {
            debugFolder = new File(SystemUtils.getGameFolder(), "debug");
            if(!debugFolder.exists())
                debugFolder.mkdirs();
        }
    }

    public static File getFolder()
    {
        return debugFolder;
    }
}
