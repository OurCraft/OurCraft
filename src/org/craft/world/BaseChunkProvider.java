package org.craft.world;

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
        return chunkMap.getAt(chunkX, chunkY, chunkZ);
    }

    public void addChunk(World world, Chunk c)
    {
        chunkMap.add(c);
    }

}
