package org.craft.world.biomes;

import java.util.*;

import com.google.common.collect.*;

import org.craft.world.*;

public abstract class Biome
{

    private List<IWorldPopulator> populators;

    public Biome()
    {
        populators = Lists.newArrayList();
        initPopulators();
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
}
