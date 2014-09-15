package org.craft.utils;

public class SystemUtils
{

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
}
