package org.craft.launch;

import java.io.File;
import java.net.URLClassLoader;

import org.craft.client.OurCraft;
import org.craft.loader.OurClassLoader;

public class OurCraftLauncher
{
    private static OurClassLoader classLoader;

    public static void main(String[] args)
    {
        classLoader = new OurClassLoader(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs());
        Thread.currentThread().setContextClassLoader(classLoader);
        try
        {
            OurCraft instance = new OurCraft();
            LWJGLSetup.load(new File(instance.getGameFolder(), "natives"));
            instance.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
