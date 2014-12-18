package org.craft.world.biomes;

import java.util.*;

import com.google.common.collect.*;

import org.craft.world.*;

public abstract class Biome
{

    private List<IWorldPopulator> populators;
    private String                id;
    private short                 uid;
    private float                 temperature;

    public Biome(String id, float temp)
    {
        this.temperature = temp;
        this.id = id;
        populators = Lists.newArrayList();
    }

    public float getTemperature()
    {
        return temperature;
    }

    public String getID()
    {
        return id;
    }

    public abstract void initPopulators();

    public void addPopulator(IWorldPopulator populator)
    {
        populators.add(populator);
    }

    public List<IWorldPopulator> getPopulators()
    {
        return populators;
    }

    public void setUniqueID(short s)
    {
        uid = s;
    }

    public short getUniqueID()
    {
        return uid;
    }
}
