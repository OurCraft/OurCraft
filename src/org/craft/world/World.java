package org.craft.world;

import org.craft.blocks.*;

public class World
{

    private ChunkMap chunkMap;

    public World()
    {
        this.chunkMap = new ChunkMap();
    }

    public Chunk getChunk(int x, int y, int z)
    {
        return chunkMap.getAt((int)Math.floor((float)x / 16f), (int)Math.floor((float)y / 16f), (int)Math.floor((float)z / 16f));
    }

    public void addChunk(Chunk c)
    {
        chunkMap.add(c);
    }

    public Block getBlock(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null) return null;
        return c.getBlock(this, x, y, z);
    }

    public void setBlock(int x, int y, int z, Block block)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null) return;
        c.setBlock(this, x, y, z, block);
    }
}
