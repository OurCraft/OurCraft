package org.craft.client.start;

import java.net.*;
import java.util.*;

import org.craft.client.*;
import org.craft.loader.*;

public class OurCraftStartup
{

    public static void start(HashMap<String, String> properties)
    {
        OurClassLoader classLoader = new OurClassLoader(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs());
        Thread.currentThread().setContextClassLoader(classLoader);
        try
        {
            OurCraft instance = new OurCraft(classLoader);
            instance.start(properties);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
