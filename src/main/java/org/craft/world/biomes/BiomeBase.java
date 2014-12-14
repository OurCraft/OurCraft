package org.craft.world.biomes;

import org.craft.world.populators.*;

public class BiomeBase extends Biome
{

    public BiomeBase()
    {
        super("base", 273f + 15f);
    }

    @Override
    public void initPopulators()
    {
        addPopulator(new RockPopulator());
        addPopulator(new GrassPopulator());
        addPopulator(new TreePopulator());
        addPopulator(new FlowerPopulator());
    }

}
