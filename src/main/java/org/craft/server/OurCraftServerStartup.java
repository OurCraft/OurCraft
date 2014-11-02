package org.craft.server;

import java.util.*;

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
                current = arg.substring(2);
            }
            else
            {
                properties.put(current, arg);
            }
        }
        OurCraftServer instance = new OurCraftServer();
        instance.start(properties);
    }

}
