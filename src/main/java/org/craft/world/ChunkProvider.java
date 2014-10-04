package org.craft.world;

import java.util.*;

public abstract class ChunkProvider implements Iterable<Chunk>
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

    /**
     * Checks to see if a chunk exists at x, y, z on specified World
     */
    public abstract boolean doesChunkExists(World world, int chunkX, int chunkY, int chunkZ);

    public abstract void addChunk(World w, Chunk c);

    public abstract Iterator<Chunk> iterator();
}
