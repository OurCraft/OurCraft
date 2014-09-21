package org.craft.world;

public abstract class ChunkProvider
{

    public ChunkProvider()
    {

    }

    /**
     * Gets a Chunk. If this chunk is null, then create one
     */
    public abstract Chunk getOrCreate(World world, int chunkX, int chunkY, int chunkZ);

    /**
     * Gets a Chunk from given chunk coordinates
     */
    public abstract Chunk get(World world, int chunkX, int chunkY, int chunkZ);

    /**
     * Creates a Chunk at given chunk coordinates
     */
    public abstract Chunk create(World world, int chunkX, int chunkY, int chunkZ);

    public abstract void addChunk(World w, Chunk c);
}
