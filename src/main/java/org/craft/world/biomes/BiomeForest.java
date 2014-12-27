package org.craft.world.biomes;

import org.craft.world.populators.*;

public class BiomeForest extends Biome
{

    public BiomeForest()
    {
        super("forest", 273f + 15f);
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
