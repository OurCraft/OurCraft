package org.craft.world.biomes;

import java.util.*;

import org.craft.blocks.*;
import org.craft.world.*;
import org.craft.world.populators.*;

public class BiomeTest5 extends Biome
{

    public BiomeTest5()
    {
        super("base_test5", 273f + 100f);
    }

    @Override
    public void initPopulators()
    {
        addPopulator(new RockPopulator());
        addPopulator(new IWorldPopulator()
        {

            @Override
            public boolean populate(World world, Chunk c, Random rng)
            {
                if(c.getCoords().y == 11)
                    for(int y = 0; y < 14; y++ )
                    {
                        for(int x = 0; x < 16; x++ )
                            for(int z = 0; z < 16; z++ )
                                c.setChunkBlock(x, y, z, Blocks.dirt);
                    }
                return true;
            }
        });
    }

}
