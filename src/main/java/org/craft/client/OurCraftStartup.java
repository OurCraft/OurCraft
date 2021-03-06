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
        Thread.currentThread().setName("Client");
        if(!(ClassLoader.getSystemClassLoader() instanceof OurClassLoader))
        {
            new CrashReport("Wrong classloader at launch. Please add -Djava.system.class.loader=org.craft.OurClassLoader in VM arguments").printStack();
            System.exit(-2);
        }
        Map<String, String> properties = Startup.argsToMap(args);
        if(!properties.containsKey("username"))
            properties.put("username", "Player_" + (int) (Math.random() * 100000L));
        if(!properties.containsKey("lang"))
            properties.put("lang", "en_US");
        if(!properties.containsKey("gamefolder"))
            properties.put("gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
        if(!properties.containsKey("nativesFolder"))
            properties.put("nativesFolder", new File(SystemUtils.getGameFolder(), "natives").getAbsolutePath());

        Startup.applyArguments(properties);
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
        System.setProperty("net.java.games.input.librarypath", properties.get("nativesFolder"));
        System.setProperty("org.lwjgl.librarypath", properties.get("nativesFolder"));

        I18n.setCurrentLanguage(properties.get("lang"));
        OurCraft instance = new OurCraft();
        CommonHandler.setInstance(instance);
        instance.start(properties);
    }
}
