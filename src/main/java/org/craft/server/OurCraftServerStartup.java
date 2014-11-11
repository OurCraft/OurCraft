package org.craft.server;

import java.util.*;

import org.craft.*;

public class OurCraftServerStartup
{

    public static void main(String[] args)
    {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("port", "35565");
        properties.put("nogui", "false");
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
        OurCraftServer instance = new OurCraftServer();
        instance.start(properties);
    }

}
