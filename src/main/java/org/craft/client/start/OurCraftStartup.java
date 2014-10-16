package org.craft.client.start;

import java.io.File;
import java.net.URLClassLoader;
import java.util.HashMap;

import org.craft.client.OurCraft;
import org.craft.loader.OurClassLoader;
import org.craft.utils.SystemUtils;

public class OurCraftStartup
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
            HashMap<String, String> properties = new HashMap<String, String>();
            String current = null;
            properties.put("username", "Player_" + (int) (Math.random() * 100000L));
            properties.put("lang", "en_US");
            for(int i = 0; i < args.length; i++ )
            {
                String arg = args[i];
                if(arg.startsWith("-"))
                {
                    current = arg.substring(1);
                }
                else
                {
                    properties.put(current, arg);
                }
            }
            OurCraft instance = new OurCraft();
            instance.start(properties);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static ClassLoader getClassLoader()
    {
        return classLoader;
    }
}
