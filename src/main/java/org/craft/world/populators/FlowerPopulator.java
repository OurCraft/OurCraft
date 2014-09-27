package org.craft.world.populators;

import java.util.*;

import org.craft.blocks.*;
import org.craft.world.*;

public class FlowerPopulator implements IWorldPopulator
{

    @Override
    public boolean populate(World world, Chunk c, Random rng)
    {
        int n = rng.nextInt(9);
        for(int i = 0; i < n; i++ )
        {
            int x = rng.nextInt(8);
            int z = rng.nextInt(8);
            int y = c.getHighest(x, z);
            if(c.getHighestBlock(x, z) == Blocks.grass)
            {
                world.setBlock(x + c.getCoords().x * 16, y + 1 + c.getCoords().y * 16, z + c.getCoords().z * 16, Blocks.rose);
            }
        }
        return true;
    }

}
