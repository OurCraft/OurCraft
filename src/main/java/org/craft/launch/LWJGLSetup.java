package org.craft.launch;

import java.io.*;

import org.craft.utils.*;

public class LWJGLSetup
{

    private static boolean loaded;

    /**
     * Load LWJGL in given folder
     */
    public static void load(File folder) throws Exception
    {
        if(!loaded)
        {
            if(!folder.exists())
                folder.mkdirs();
            if(folder.isDirectory())
            {
                installNatives(folder);
                System.setProperty("org.lwjgl.librarypath", folder.getAbsolutePath());
            }
            loaded = true;
        }
    }

    /**
     * Installs natives into given folder
     */
    private static void installNatives(File folder) throws Exception
    {
        OperatingSystem os = SystemUtils.getOS();
        if(os == OperatingSystem.WINDOWS)
        {
            if(!new File(folder.getPath() + "/jinput-dx8_64.dll").exists())
            {
                extractFromClasspath("jinput-dx8_64.dll", folder);
                extractFromClasspath("jinput-dx8.dll", folder);
                extractFromClasspath("jinput-raw_64.dll", folder);
                extractFromClasspath("jinput-raw.dll", folder);
                extractFromClasspath("lwjgl.dll", folder);
                extractFromClasspath("lwjgl64.dll", folder);
                extractFromClasspath("OpenAL32.dll", folder);
                extractFromClasspath("OpenAL64.dll", folder);
            }
            else
            {
                Log.message("Natives already exist.");
            }
        }
        else if(os == OperatingSystem.SOLARIS)
        {
            if(!new File(folder.getPath() + "/liblwjgl.so").exists())
            {
                extractFromClasspath("liblwjgl.so", folder);
                extractFromClasspath("liblwjgl64.so", folder);
                extractFromClasspath("libopenal.so", folder);
                extractFromClasspath("libopenal64.so", folder);
            }
            else
            {
                Log.message("Natives already exist.");
            }

        }
        else if(os == OperatingSystem.LINUX)
        {
            if(!new File(folder.getPath() + "/liblwjgl.so").exists())
            {
                extractFromClasspath("liblwjgl.so", folder);
                extractFromClasspath("liblwjgl64.so", folder);
                extractFromClasspath("libopenal.so", folder);
                extractFromClasspath("libopenal64.so", folder);
            }
            else
            {
                Log.message("Natives already exist.");
            }

        }
        else if(os == OperatingSystem.MACOSX)
        {
            if(!new File(folder.getPath() + "/openal.dylib").exists())
            {
                extractFromClasspath("liblwjgl.jnilib", folder);
                extractFromClasspath("liblwjgl-osx.jnilib", folder);
                extractFromClasspath("openal.dylib", folder);
            }
            else
            {
                Log.message("Natives already exist.");
            }
        }
        else
        {
        }
        System.setProperty("net.java.games.input.librarypath", folder.getAbsolutePath());
    }

    private static void extractFromClasspath(String fileName, File folder)
    {
        try
        {
            FileOutputStream in = new FileOutputStream(new File(folder, fileName));
            IOUtils.copy(LWJGLSetup.class.getResourceAsStream("/" + fileName), in);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
