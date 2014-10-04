package org.craft.world;

import java.io.*;
import java.util.*;

public class BaseChunkProvider extends ChunkProvider
{

    private ChunkMap    chunkMap;
    private WorldLoader loader;

    public BaseChunkProvider(WorldLoader loader)
    {
        this.loader = loader;
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

    @Override
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
        try
        {
            Chunk chunk = loader.loadChunk(world, chunkX, chunkY, chunkZ);
            if(chunk != null)
            {
                addChunk(world, chunk);
                return chunk;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        Chunk newChunk = new Chunk(world, new ChunkCoord(chunkX, chunkY, chunkZ));
        addChunk(world, newChunk);
        world.getGenerator().populateChunk(world, newChunk);
        newChunk.markDirty();
        return newChunk;
    }

    @Override
    public boolean doesChunkExists(World world, int chunkX, int chunkY, int chunkZ)
    {
        return chunkMap.contains(ChunkCoord.get(chunkX, chunkY, chunkZ));
    }

    @Override
    public Iterator<Chunk> iterator()
    {
        return chunkMap.iterator();
    }
}
