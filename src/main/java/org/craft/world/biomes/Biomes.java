package org.craft.world.biomes;

import org.craft.world.populators.*;

public class Biomes
{

    public static final Biome BASE = new Biome()
                                   {

                                       @Override
                                       public void initPopulators()
                                       {
                                           addPopulator(new RockPopulator());
                                           addPopulator(new GrassPopulator());
                                           addPopulator(new TreePopulator());
                                           addPopulator(new FlowerPopulator());
                                       }
                                   };
}
