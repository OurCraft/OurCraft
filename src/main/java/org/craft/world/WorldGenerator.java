package org.craft.world;

import java.util.*;

import org.craft.blocks.*;
import org.craft.maths.*;
import org.craft.world.biomes.*;

public class WorldGenerator
{

    private long   seed;
    private Random rng;

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
        if(chunk.getBiome() == null)
        {
            float temperature = MathHelper.perlinNoise(chunk.getCoords().x * 8, chunk.getCoords().z * 8, seed);
            chunk.setBiome(Biomes.getClosestFromNoise(temperature));
        }
        Biome biome = chunk.getBiome();
        for(IWorldPopulator populator : biome.getPopulators())
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

    public long getSeed()
    {
        return seed;
    }

    public void setSeed(long seed)
    {
        this.seed = seed;
    }

}
