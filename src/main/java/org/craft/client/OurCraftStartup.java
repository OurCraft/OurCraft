package org.craft.client;

import java.io.*;
import java.net.*;
import java.util.*;

import org.craft.*;
import org.craft.utils.*;

public class OurCraftStartup
{

    public static void main(String[] args)
    {
        OurClassLoader classLoader = new OurClassLoader(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs());
        Thread.currentThread().setContextClassLoader(classLoader);
        try
        {
            HashMap<String, String> properties = new HashMap<String, String>();
            properties.put("username", "Player_" + System.currentTimeMillis());
            properties.put("lang", "en_US");
            properties.put("gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
            properties.put("nativesFolder", SystemUtils.getGameFolder().getAbsolutePath() + "/natives");
            String current = null;
            for(int i = 0; i < args.length; i++ )
            {
                String arg = args[i];
                if(arg.startsWith("--"))
                {
                    current = arg.substring(2);
                }
                else
                {
                    properties.put(current, arg);
                }
            }
            SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
            System.setProperty("net.java.games.input.librarypath", properties.get("nativesFolder"));
            System.setProperty("org.lwjgl.librarypath", properties.get("nativesFolder"));
            OurCraft instance = new OurCraft(classLoader);
            instance.start(properties);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
