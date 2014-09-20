package org.craft.world;

import org.craft.utils.*;

public class BaseChunkProvider extends ChunkProvider
{

    private ChunkMap chunkMap;

    public BaseChunkProvider()
    {
        chunkMap = new ChunkMap();
    }

    @Override
    public Chunk getOrCreate(World world, int chunkX, int chunkY, int chunkZ)
    {
        Chunk c = chunkMap.getAt(chunkX, chunkY, chunkZ);
        if(c == null)
        {
            Chunk newChunk = new Chunk(new ChunkCoord(chunkX, chunkY, chunkZ));
            world.getGenerator().populateChunk(world, newChunk);
            addChunk(world, newChunk);
            newChunk.markDirty();
            Log.message("Creating new chunk at " + chunkX + "," + chunkY + "," + chunkZ);
            return newChunk;
        }
        return c;
    }

    public void addChunk(World world, Chunk c)
    {
        chunkMap.add(c);
    }

    @Override
    public Chunk get(World world, int chunkX, int chunkY, int chunkZ)
    {
        return chunkMap.getAt(chunkX, chunkY, chunkZ);
    }

    @Override
    public Chunk create(World world, int chunkX, int chunkY, int chunkZ)
    {
        Chunk newChunk = new Chunk(new ChunkCoord(chunkX, chunkY, chunkZ));
        newChunk.markDirty();
        addChunk(world, newChunk);
        world.getGenerator().populateChunk(world, newChunk);
        return newChunk;
    }

}
