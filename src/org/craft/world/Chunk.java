package org.craft.world;

import java.util.*;

import org.craft.blocks.*;

public class Chunk
{

    public String[][][] blocks;

    public Chunk()
    {
        this.blocks = new String[16][16][16];
        for(int x = 0; x < 16; x++ )
            for(int y = 0; y < 16; y++ )
                Arrays.fill(blocks[x][y], Blocks.air.getID());
    }

    public Block getBlock(World w, int worldX, int worldY, int worldZ)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;
        return Blocks.get(blocks[x][y][z]);
    }

    public void setBlock(World world, int worldX, int worldY, int worldZ, Block block)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;
        blocks[x][y][z] = block.getID();
    }
}
