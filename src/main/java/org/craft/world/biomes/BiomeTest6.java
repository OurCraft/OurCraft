package org.craft.world.biomes;

import java.util.*;

import org.craft.blocks.*;
import org.craft.world.*;
import org.craft.world.populators.*;

public class BiomeTest6 extends Biome
{

    public BiomeTest6()
    {
        super("base_test6", 273f + 125f);
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
                    for(int y = 0; y < 16; y++ )
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
