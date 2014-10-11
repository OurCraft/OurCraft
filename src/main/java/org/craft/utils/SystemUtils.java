package org.craft.utils;

import java.io.*;

public class SystemUtils
{

    public static enum OperatingSystem
    {
        WINDOWS, LINUX, MACOSX, SOLARIS, UNKNOWN;
    }

    public static OperatingSystem getOS()
    {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win"))
        {
            return OperatingSystem.WINDOWS;
        }
        if(os.contains("sunos") || os.contains("solaris"))
        {
            return OperatingSystem.SOLARIS;
        }
        if(os.contains("unix"))
        {
            return OperatingSystem.LINUX;
        }
        if(os.contains("linux"))
        {
            return OperatingSystem.LINUX;
        }
        if(os.contains("mac"))
        {
            return OperatingSystem.MACOSX;
        }
        return OperatingSystem.UNKNOWN;
    }

    public static String getUserName()
    {
        return System.getProperty("user.name");
    }

    /**
     * Returns the folder where game data is saved
     */
    public static File getGameFolder()
    {
        File gameFolder = null;
        String appdata = System.getenv("APPDATA");
        if(appdata != null)
            gameFolder = new File(appdata, ".ourcraft");
        else
            gameFolder = new File(System.getProperty("user.home"), ".ourcraft");
        return gameFolder;
    }

    public static void deleteRecursivly(File file)
    {
        if(file.isDirectory())
        {
            File[] list = file.listFiles();
            if(list != null)
                for(File f : list)
                {
                    deleteRecursivly(f);
                    f.delete();
                }
        }
        file.delete();
    }
}
