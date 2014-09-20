package org.craft.world;

import java.util.*;

import org.craft.blocks.*;

public abstract class WorldGenerator
{

    private long                       seed;
    private World                      world;
    private ArrayList<IWorldPopulator> populators = new ArrayList<>();
    private Random                     rng;

    public WorldGenerator(World w)
    {
        this(w, System.currentTimeMillis());
    }

    public WorldGenerator(World w, long seed)
    {
        this.world = w;
        this.seed = seed;
        this.rng = new Random(seed);
    }

    public boolean populateChunk(Chunk chunk)
    {
        if(chunk == null) return false;
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

}
