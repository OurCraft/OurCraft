package org.craft.launch;

import java.io.File;
import java.net.URLClassLoader;

import org.craft.client.OurCraft;
import org.craft.loader.OurClassLoader;
import org.craft.utils.SystemUtils;

public class OurCraftLauncher
{
    private static OurClassLoader classLoader;

    public static void main(String[] args)
    {
        classLoader = new OurClassLoader(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs());
        Thread.currentThread().setContextClassLoader(classLoader);
        try
        {
            final File gameFolder = SystemUtils.getGameFolder();
            LWJGLSetup.load(new File(gameFolder, "natives"));
            OurCraft instance = new OurCraft(gameFolder);
            instance.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
