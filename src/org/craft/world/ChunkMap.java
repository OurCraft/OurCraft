package org.craft.world;

import java.util.*;

public class ChunkMap implements Iterable<Chunk>
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

    @Override
    public Iterator<Chunk> iterator()
    {
        return map.values().iterator();
    }

    public void add(Chunk c)
    {
        map.put(c.getCoords(), c);
    }
}
