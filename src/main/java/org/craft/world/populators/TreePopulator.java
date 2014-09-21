package org.craft.world.populators;

import java.util.*;

import org.craft.blocks.*;
import org.craft.utils.*;
import org.craft.world.*;

public class TreePopulator implements IWorldPopulator
{

    @Override
    public boolean populate(World world, Chunk c, Random rng)
    {
        int n = rng.nextInt(8);
        for(int i = 0; i < n; i++ )
        {
            int x = rng.nextInt(8);
            int z = rng.nextInt(8);
            int y = c.getHighest(x, z);
            boolean noBlockAside = true;
            if(world.getBlockNextTo(x, y + 1, z, EnumSide.NORTH) == Blocks.log)
                noBlockAside = false;
            if(world.getBlockNextTo(x, y + 1, z, EnumSide.SOUTH) == Blocks.log)
                noBlockAside = false;
            if(world.getBlockNextTo(x, y + 1, z, EnumSide.EAST) == Blocks.log)
                noBlockAside = false;
            if(world.getBlockNextTo(x, y + 1, z, EnumSide.WEST) == Blocks.log)
                noBlockAside = false;
            if(c.getHighestBlock(x, z) == Blocks.grass && noBlockAside)
            {
                for(int yy = 0; yy < 2; yy++ )
                {
                    for(int xx = -1; xx <= 1; xx++ )
                    {
                        for(int zz = -1; zz <= 1; zz++ )
                        {
                            world.setBlock(xx + x + c.getCoords().x * 16, yy + y + 3 + c.getCoords().y * 16, zz + z + c.getCoords().z * 16, Blocks.leaves);
                        }
                    }
                }
                for(int j = 1; j < 4; j++ )
                    c.setChunkBlock(x, y + j, z, Blocks.log);
                world.setBlock(x + c.getCoords().x * 16, y + 5 + c.getCoords().y * 16, z + c.getCoords().z * 16, Blocks.leaves);
            }
        }
        return true;
    }

}
