package org.craft.world.populators;

import java.util.*;

import org.craft.blocks.*;
import org.craft.maths.*;
import org.craft.world.*;

public class MountainsPopulator implements IWorldPopulator
{

    private int   startChunkY;
    private int   maxHeight;
    private Block surfaceBlock;
    private Block interiorBlock;
    private Block belowSurfaceBlock;

    public MountainsPopulator(Block stone, Block belowSurfaceBlock, Block surfaceBlock, int maxHeight, int i)
    {
        this.interiorBlock = stone;
        this.surfaceBlock = surfaceBlock;
        this.belowSurfaceBlock = belowSurfaceBlock;
        this.maxHeight = maxHeight;
        this.startChunkY = i;
    }

    @Override
    public boolean populate(World world, Chunk c, Random rng)
    {
        if(c.getCoords().y >= startChunkY)
        {
            int heightOffset = (c.getCoords().y - startChunkY) * 16;
            for(int x = 0; x < 16; x++ )
            {
                for(int z = 0; z < 16; z++ )
                {
                    float noiseVal = Math.abs(MathHelper.perlinNoise(x + c.getCoords().x * 16, z + c.getCoords().z * 16, world.getSeed()));
                    float height = noiseVal * maxHeight;
                    int maxY = (int) Math.round(height - heightOffset);

                    int depthness = (int) ((int) noiseVal / 5f * (maxHeight - height));
                    for(int y = 0; y <= maxY && y < 16; y++ )
                    {
                        Block block = interiorBlock;
                        if(y == maxY)
                            block = surfaceBlock;
                        else if(y >= maxY - depthness)
                            block = belowSurfaceBlock;
                        c.setChunkBlock(x, y, z, block);
                    }
                }
            }
            return true;
        }
        return false;
    }

}
