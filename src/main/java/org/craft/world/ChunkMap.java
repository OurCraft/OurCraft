package org.craft.world;

import java.util.*;

public class ChunkMap implements Iterable<Chunk>
{

    private HashMap<ChunkCoord, Chunk> map;

    public ChunkMap()
    {
        map = new HashMap<ChunkCoord, Chunk>();
    }

    /**
     * Returns Chunk from given ChunkCoords
     */
    public Chunk getAt(int chunkX, int chunkY, int chunkZ)
    {
        return map.get(ChunkCoord.get(chunkX, chunkY, chunkZ));
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

    public boolean contains(ChunkCoord coords)
    {
        return map.containsKey(coords);
    }
}
