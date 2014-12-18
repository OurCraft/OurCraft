package org.craft.world.biomes;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.*;

public class Biomes
{

    public static final HashMap<String, Biome> BIOME_REGISTRY = Maps.newHashMap();
    private static List<Biome>                 biomeByID      = Lists.newArrayList();
    private static float                       maxTemperature;
    private static float                       minTemperature;

    public static final Biome                  BASE           = new BiomeBase();
    public static final Biome                  MOUNTAINS      = new BiomeMountains("mountains", 273f - 2f, 100, Blocks.grass);
    public static final Biome                  BASE_TEST      = new BiomeTest();
    public static final Biome                  BASE_TEST2     = new BiomeTest2();
    public static final Biome                  BASE_TEST3     = new BiomeTest3();

    public static void init()
    {
        minTemperature = Float.POSITIVE_INFINITY;
        maxTemperature = Float.NEGATIVE_INFINITY;
        register(BASE);
        register(MOUNTAINS);
        register(BASE_TEST);
        register(BASE_TEST2);
        register(BASE_TEST3);

        for(short i = 0; i < biomeByID.size(); i++ )
        {
            Biome b = biomeByID.get(i);
            if(b != null)
                b.setUniqueID(i);
        }
    }

    /**
     * Registers a block into the BLOCK_REGISTRY field
     */
    public static void register(Biome biome)
    {
        if(BIOME_REGISTRY.containsKey(biome.getID()))
        {
            throw new IllegalArgumentException("Id " + biome.getID() + " is already used by " + BIOME_REGISTRY.get(biome.getID()) + " when trying to add " + biome);
        }
        BIOME_REGISTRY.put(biome.getID(), biome);
        biomeByID.add(biome);
        biome.initPopulators();
        if(maxTemperature < biome.getTemperature())
            maxTemperature = biome.getTemperature();
        if(minTemperature > biome.getTemperature())
            minTemperature = biome.getTemperature();
    }

    /**
     * Returns the biome in BIOME_REGISTRY with given id
     */
    public static Biome get(String string)
    {
        if(string == null)
            return BASE;
        return BIOME_REGISTRY.get(string);
    }

    /**
     * Returns a biome depending on its UID
     */
    public static Biome getByID(short id)
    {
        Biome b = biomeByID.get(id);
        if(b == null)
            b = BASE;
        return b;
    }

    public static List<Biome> getBiomes()
    {
        return biomeByID;
    }

    public static Biome getClosestFromNoise(float noiseValue)
    {
        float temp = noiseValue * (maxTemperature - minTemperature) + minTemperature;
        Biome closest = null;
        float minDiff = Float.POSITIVE_INFINITY;
        for(Biome biome : getBiomes())
        {
            float diff = Math.abs(biome.getTemperature() - temp);
            if(diff < minDiff)
            {
                minDiff = diff;
                closest = biome;
            }
        }
        return closest;
    }

    public static float normalizeTemperature(float temperature)
    {
        return (temperature - minTemperature) / (maxTemperature - minTemperature);
    }
}
