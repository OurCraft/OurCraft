package org.craft.world;

public abstract class ChunkProvider
{

    public ChunkProvider()
    {

    }

    public abstract Chunk getOrCreate(World world, int chunkX, int chunkY, int chunkZ);

    public abstract void addChunk(World w, Chunk c);
}
