package org.craft.launch;

import java.io.*;

import org.craft.utils.*;

public class LWJGLSetup
{

    private static boolean loaded;

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

    private static void installNatives(File folder) throws Exception
    {
        OperatingSystem os = SystemUtils.getOS();
        if(os == OperatingSystem.WINDOWS)
        {
            if(!new File(folder.getPath() + "/jinput-dx8_64.dll").exists())
            {
                extractFromClasspath("/windows/jinput-dx8_64.dll", folder);
                extractFromClasspath("/windows/jinput-dx8.dll", folder);
                extractFromClasspath("/windows/jinput-raw_64.dll", folder);
                extractFromClasspath("/windows/jinput-raw.dll", folder);
                extractFromClasspath("/windows/lwjgl.dll", folder);
                extractFromClasspath("/windows/lwjgl64.dll", folder);
                extractFromClasspath("/windows/OpenAL32.dll", folder);
                extractFromClasspath("/windows/OpenAL64.dll", folder);
            }
            else
            {
                Log.message("Natives already exist.");
            }
        }
        else
            if(os == OperatingSystem.SOLARIS)
            {
                if(!new File(folder.getPath() + "/liblwjgl.so").exists())
                {
                    extractFromClasspath("/solaris/liblwjgl.so", folder);
                    extractFromClasspath("/solaris/liblwjgl64.so", folder);
                    extractFromClasspath("/solaris/libopenal.so", folder);
                    extractFromClasspath("/solaris/libopenal64.so", folder);
                }
                else
                {
                    Log.message("Natives already exist.");
                }

            }
            else
                if(os == OperatingSystem.LINUX)
                {
                    if(!new File(folder.getPath() + "/liblwjgl.so").exists())
                    {
                        extractFromClasspath("/linux/liblwjgl.so", folder);
                        extractFromClasspath("/linux/liblwjgl64.so", folder);
                        extractFromClasspath("/linux/libopenal.so", folder);
                        extractFromClasspath("/linux/libopenal64.so", folder);
                    }
                    else
                    {
                        Log.message("Natives already exist.");
                    }

                }
                else
                    if(os == OperatingSystem.MACOSX)
                    {
                        if(!new File(folder.getPath() + "/openal.dylib").exists())
                        {
                            extractFromClasspath("/macosx/liblwjgl.jnilib", folder);
                            extractFromClasspath("/macosx/liblwjgl-osx.jnilib", folder);
                            extractFromClasspath("/macosx/openal.dylib", folder);
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
        String[] split = fileName.split("/");
        String diskFileName = split[split.length - 1];
        try(FileOutputStream in = new FileOutputStream(new File(folder, diskFileName)))
        {
            IOUtils.copy(LWJGLSetup.class.getResourceAsStream(fileName), in);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
