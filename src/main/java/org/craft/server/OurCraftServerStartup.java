package org.craft.server;

import java.util.*;

import org.craft.*;
import org.craft.utils.*;

public class OurCraftServerStartup
{

    public static void main(String[] args)
    {
        Thread.currentThread().setName("Server");
        Map<String, String> properties = Startup.argsToMap(args);
        if(!properties.containsKey("port"))
            properties.put("port", "35565");
        if(!properties.containsKey("nogui"))
            properties.put("nogui", "false");
        if(!properties.containsKey("gamefolder"))
            properties.put("gamefolder", SystemUtils.getGameFolder().getAbsolutePath());
        Startup.applyArguments(properties);
        OurCraftServer instance = new OurCraftServer();
        instance.start(properties);
    }

}
