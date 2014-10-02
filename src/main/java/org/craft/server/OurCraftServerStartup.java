package org.craft.server;

import java.io.*;
import java.util.*;

import org.craft.loader.*;
import org.craft.utils.*;

public class OurCraftServerStartup
{

    private static OurClassLoader classLoader;

    public static void main(String[] args)
    {
        //        classLoader = new OurClassLoader(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs());
        //        Thread.currentThread().setContextClassLoader(classLoader);
        try
        {
            final File gameFolder = SystemUtils.getGameFolder();
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("port", "35565");
            properties.put("nogui", "false");
            String current = null;
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
            OurCraftServer instance = new OurCraftServer();
            instance.start(properties);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
