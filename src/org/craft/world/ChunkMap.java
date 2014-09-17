package org.craft.world;

import java.util.*;

public class ChunkMap
{

    private HashMap<ChunkCoord, Chunk> map;

    public ChunkMap()
    {
        map = new HashMap<>();
    }

    public Chunk getAt(int chunkX, int chunkY, int chunkZ)
    {
        return map.get(new ChunkCoord(chunkX, chunkY, chunkZ));
    }

    public void setAt(int chunkX, int chunkY, int chunkZ, Chunk chunk)
    {
        map.put(new ChunkCoord(chunkX, chunkY, chunkZ), chunk);
    }
}
