package org.craft;

import java.util.*;

import com.google.common.collect.*;

public class ParticleRegistry
{

    private static Collection<String> types;

    public static void init()
    {
        registerType("smoke");
    }

    public static void registerType(String type)
    {
        if(types == null)
        {
            types = Lists.newArrayList();
        }
        types.add(type);
    }

    public static Collection<String> getTypes()
    {
        return types;
    }
}
