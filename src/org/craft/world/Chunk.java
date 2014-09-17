package org.craft.world;

import java.util.*;

import org.craft.blocks.*;

public class Chunk
{

    public Block[][][] blocks;
    private ChunkCoord coords;
    private boolean    isDirty = false;

    public Chunk(ChunkCoord coords)
    {
        this.coords = coords;
        this.blocks = new Block[16][16][16];
        for(int x = 0; x < 16; x++ )
            for(int y = 0; y < 16; y++ )
                Arrays.fill(blocks[x][y], Blocks.air);
    }

    public Block getBlock(World w, int worldX, int worldY, int worldZ)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0) x = 16 + x;
        if(y < 0) y = 16 + y;
        if(z < 0) z = 16 + z;
        return blocks[x][y][z];
    }

    public void setBlock(World world, int worldX, int worldY, int worldZ, Block block)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0) x = 16 + x;
        if(y < 0) y = 16 + y;
        if(z < 0) z = 16 + z;
        blocks[x][y][z] = block;
        isDirty = true;
    }

    public boolean isDirty()
    {
        return isDirty;
    }

    public ChunkCoord getCoords()
    {
        return coords;
    }
}
