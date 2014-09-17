package org.craft.world;

import org.craft.blocks.*;

public class World
{

    private ChunkMap chunkMap;

    public World()
    {
        this.chunkMap = new ChunkMap();
    }

    public void setChunk(int chunkX, int chunkY, int chunkZ, Chunk c)
    {
        chunkMap.setAt(chunkX, chunkY, chunkZ, c);
    }

    public Block getBlock(int x, int y, int z)
    {
        Chunk c = chunkMap.getAt((int)Math.floor((float)x / 16f), (int)Math.floor((float)y / 16f), (int)Math.floor((float)z / 16f));
        if(c == null) return null;
        return c.getBlock(this, x, y, z);
    }

    public void setBlock(int x, int y, int z, Block block)
    {
        Chunk c = chunkMap.getAt((int)Math.floor((float)x / 16f), (int)Math.floor((float)y / 16f), (int)Math.floor((float)z / 16f));
        if(c == null) return;
        c.setBlock(this, x, y, z, block);
    }
}
