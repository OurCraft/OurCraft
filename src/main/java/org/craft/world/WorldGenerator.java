package org.craft.world;

import java.util.*;

import org.craft.blocks.*;

public class WorldGenerator
{

    private long                       seed;
    private ArrayList<IWorldPopulator> populators = new ArrayList<IWorldPopulator>();
    private Random                     rng;

    public WorldGenerator()
    {
        this(System.currentTimeMillis());
    }

    public WorldGenerator(long seed)
    {
        this.seed = seed;
        this.rng = new Random(seed);
    }

    /**
     * Generates and populates given chunk
     */
    public boolean populateChunk(World world, Chunk chunk)
    {
        if(chunk == null)
            return false;
        for(IWorldPopulator populator : populators)
        {
            populator.populate(world, chunk, rng);
        }
        if(chunk.getCoords().y == 0) // Temporary Bottom layer
        {
            for(int x = 0; x < 16; x++ )
            {
                for(int z = 0; z < 16; z++ )
                {
                    chunk.setChunkBlock(x, 0, z, Blocks.bedrock);
                }
            }
        }
        return true;
    }

    public void addPopulator(IWorldPopulator populator)
    {
        populators.add(populator);
    }

    public long getSeed()
    {
        return seed;
    }

    public void setSeed(long seed)
    {
        this.seed = seed;
    }

}
