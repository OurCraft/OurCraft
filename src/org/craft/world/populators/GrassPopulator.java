package org.craft.world.populators;

import java.util.*;

import org.craft.blocks.*;
import org.craft.maths.*;
import org.craft.world.*;

public class GrassPopulator implements IWorldPopulator
{

    @Override
    public boolean populate(World world, Chunk c, Random rng)
    {
        if(c.getCoords().y >= 11)
        {
            for(int x = 0; x < 16; x++ )
            {
                for(int z = 0; z < 16; z++ )
                {
                    int maxY = (int)Math.floor(4f * MathHelper.perlinNoise(x + c.getCoords().x * 16, z + c.getCoords().z * 16, 15) / 2) - (c.getCoords().y - 11) * 16 + 1;
                    for(int y = 0; y <= maxY && y < 16; y++ )
                    {
                        if(y == maxY)
                            c.setChunkBlock(x, y, z, Blocks.grass);
                        else
                            c.setChunkBlock(x, y, z, Blocks.dirt);
                    }
                }
            }
            return true;
        }
        return false;
    }

}
