package org.craft.world;

import java.util.*;

import org.craft.blocks.*;

public class Chunk
{

    public Block[][][] blocks;
    public float[][][] lightValues;
    private ChunkCoord coords;
    private boolean    isDirty;

    public Chunk(ChunkCoord coords)
    {
        this.coords = coords;
        this.blocks = new Block[16][16][16];
        this.lightValues = new float[16][16][16];
        for(int x = 0; x < 16; x++ )
            for(int y = 0; y < 16; y++ )
            {
                Arrays.fill(blocks[x][y], Blocks.air);
                Arrays.fill(lightValues[x][y], 1f);
            }
    }

    public float getLightValue(World w, int worldX, int worldY, int worldZ)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0) x = 16 + x;
        if(y < 0) y = 16 + y;
        if(z < 0) z = 16 + z;
        return lightValues[x][y][z];
    }

    public Block getBlock(World w, int worldX, int worldY, int worldZ)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0) x = 16 + x;
        if(y < 0) y = 16 + y;
        if(z < 0) z = 16 + z;
        if(blocks[x][y][z] == null) blocks[x][y][z] = Blocks.air;
        return blocks[x][y][z];
    }

    public void setLightValue(World world, int worldX, int worldY, int worldZ, float lightValue)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0) x = 16 + x;
        if(y < 0) y = 16 + y;
        if(z < 0) z = 16 + z;
        lightValues[x][y][z] = lightValue;
        isDirty = true;
    }

    public void setBlock(World world, int worldX, int worldY, int worldZ, Block block)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0) x = 16 + x;
        if(y < 0) y = 16 + y;
        if(z < 0) z = 16 + z;
        if(block == null) block = Blocks.air;
        blocks[x][y][z] = block;
        isDirty = true;

        if(x == 0)
        {
            Chunk c = world.getChunkProvider().get(world, coords.x - 1, coords.y, coords.z);
            if(c != null) c.markDirty();
        }

        if(x == 15)
        {
            Chunk c = world.getChunkProvider().get(world, coords.x + 1, coords.y, coords.z);
            if(c != null) c.markDirty();
        }

        if(y == 0)
        {
            Chunk c = world.getChunkProvider().get(world, coords.x, coords.y - 1, coords.z);
            if(c != null) c.markDirty();
        }

        if(y == 15)
        {
            Chunk c = world.getChunkProvider().get(world, coords.x, coords.y + 1, coords.z);
            if(c != null) c.markDirty();
        }

        if(z == 0)
        {
            Chunk c = world.getChunkProvider().get(world, coords.x, coords.y, coords.z - 1);
            if(c != null) c.markDirty();
        }

        if(z == 15)
        {
            Chunk c = world.getChunkProvider().get(world, coords.x, coords.y, coords.z + 1);
            if(c != null) c.markDirty();
        }
    }

    public boolean isDirty()
    {
        return isDirty;
    }

    public ChunkCoord getCoords()
    {
        return coords;
    }

    public void markDirty()
    {
        isDirty = true;
    }

    public void cleanUpDirtiness()
    {
        isDirty = false;
    }

    public void fill(Block block)
    {
        for(int x = 0; x < 16; x++ )
            for(int y = 0; y < 16; y++ )
            {
                for(int z = 0; z < 16; z++ )
                {
                    setChunkBlock(x, y, z, block);
                }
            }
    }

    public float getChunkLightValue(int x, int y, int z, float lightValue)
    {
        return lightValues[x][y][z];
    }

    public void setChunkLightValue(int x, int y, int z, float lightValue)
    {
        lightValues[x][y][z] = lightValue;
        markDirty();
    }

    public Block getChunkBlock(int x, int y, int z, Block block)
    {
        Block b = blocks[x][y][z];
        if(b == null) return Blocks.air;
        return b;
    }

    public void setChunkBlock(int x, int y, int z, Block block)
    {
        if(block == null)
            block = Blocks.air;
        else
            blocks[x][y][z] = block;
        markDirty();
    }
}
