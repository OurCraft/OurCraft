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
        Chunk c = get(world, chunkX, chunkY, chunkZ);
        if(c == null)
        {
            return create(world, chunkX, chunkY, chunkZ);
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
        addChunk(world, newChunk);
        newChunk.markDirty();
        world.getGenerator().populateChunk(world, newChunk);
        return newChunk;
    }

}
