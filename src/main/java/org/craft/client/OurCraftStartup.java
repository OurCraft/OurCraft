package org.craft.client;

import java.io.*;
import java.util.*;

import org.craft.*;
import org.craft.utils.*;
import org.craft.utils.crash.*;

public class OurCraftStartup
{

    public static void main(String[] args)
    {
        if(!(ClassLoader.getSystemClassLoader() instanceof OurClassLoader))
        {
            new CrashReport("Wrong classloader at launch. Please add -Djava.system.class.loader=org.craft.OurClassLoader in VM arguments").printStack();
            System.exit(-2);
        }
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
                if(current != null && !properties.containsKey(current))
                {
                    properties.put(current, "");
                }
                current = arg.substring(2);
            }
            else
            {
                properties.put(current, arg);
            }
        }
        if(current != null && !properties.containsKey(current))
        {
            properties.put(current, "");
        }
        Commons.applyArguments(properties);
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
        System.setProperty("net.java.games.input.librarypath", properties.get("nativesFolder"));
        System.setProperty("org.lwjgl.librarypath", properties.get("nativesFolder"));

        OurCraft instance = new OurCraft();
        instance.start(properties);
    }
}
