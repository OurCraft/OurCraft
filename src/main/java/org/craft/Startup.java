package org.craft;

import java.io.*;
import java.util.*;

import org.craft.client.models.*;
import org.craft.client.render.*;
import org.craft.modding.modifiers.*;
import org.craft.utils.*;
import org.reflections.*;

public final class Startup
{
    private Startup()
    {
        throw new IllegalAccessError("No instance for you!");
    }

    public static void loadTransformers()
    {
        ModifierClassTransformer modTrans = new ModifierClassTransformer();
        OurClassLoader.instance.addTransformer(modTrans);
        Reflections reflections = new Reflections(OurClassLoader.instance);
        for(Class<?> c : reflections.getSubTypesOf(ASMTransformerPlugin.class))
        {
            try
            {
                ASMTransformerPlugin transformers = (ASMTransformerPlugin) c.newInstance();
                transformers.registerModifiers(modTrans);
            }
            catch(InstantiationException e)
            {
                e.printStackTrace();
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> argsToMap(String[] args)
    {
        String current = null;
        HashMap<String, String> properties = new HashMap<String, String>();
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

        return properties;
    }

    public static void applyArguments(Map<String, String> properties)
    {
        boolean debug = properties.get("debug") != null && !properties.get("debug").equalsIgnoreCase("false");
        Log.useFullClassNames = debug;
        Log.showCaller = debug;
        ModifierClassTransformer.debug = debug;
        RenderEngine.debug = debug;
        RenderItems.debug = debug;
        ModelLoader.debug = debug;
        SystemUtils.setGameFolder(new File(properties.get("gamefolder")));
    }
}
