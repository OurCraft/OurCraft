package org.craft.world;

import java.util.*;

import com.google.common.base.Optional;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.maths.*;
import org.craft.spongeimpl.math.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.math.*;
import org.spongepowered.api.world.biome.*;

public class Chunk implements org.spongepowered.api.world.Chunk
{

    public short[][][]                         blocks;
    public int[][]                             highest;
    public float[][][]                         lightValues;
    public HashMap<Vector3, BlockStatesObject> blockStatesObjects;
    private ChunkCoord                         coords;
    private boolean                            isDirty;
    private World                              owner;

    public Chunk(World owner, ChunkCoord coords)
    {
        this.owner = owner;
        this.coords = coords;
        this.blocks = new short[16][16][16];
        this.highest = new int[16][16];
        this.lightValues = new float[16][16][16];
        blockStatesObjects = new HashMap<Vector3, BlockStatesObject>();
        for(int x = 0; x < 16; x++ )
        {
            Arrays.fill(highest[x], -1);
            for(int y = 0; y < 16; y++ )
            {
                Arrays.fill(blocks[x][y], Blocks.air.getUniqueID());
                Arrays.fill(lightValues[x][y], 1f);
            }
        }
    }

    /**
     * Returns light value in Chunk from given world space
     */
    public float getLightValue(World w, int worldX, int worldY, int worldZ)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0)
            x = 16 + x;
        if(y < 0)
            y = 16 + y;
        if(z < 0)
            z = 16 + z;
        return lightValues[x][y][z];
    }

    /**
     * Returns block in Chunk from given world space
     */
    public Block getBlock(World w, int worldX, int worldY, int worldZ)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0)
            x = 16 + x;
        if(y < 0)
            y = 16 + y;
        if(z < 0)
            z = 16 + z;

        return Blocks.getByID(blocks[x][y][z]);
    }

    /**
     * Sets light value in Chunk from given world space
     */
    public void setLightValue(World world, int worldX, int worldY, int worldZ, float lightValue)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0)
            x = 16 + x;
        if(y < 0)
            y = 16 + y;
        if(z < 0)
            z = 16 + z;
        lightValues[x][y][z] = lightValue;
        isDirty = true;

        markNeighbors(x, y, z);
    }

    /**
     * Set block in Chunk from given world space
     */
    public void setBlock(World world, int worldX, int worldY, int worldZ, Block block)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0)
            x = 16 + x;
        if(y < 0)
            y = 16 + y;
        if(z < 0)
            z = 16 + z;
        setChunkBlock(x, y, z, block);
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
                    markNeighbors(x, y, z);
                }
            }
    }

    /**
     * Returns light value in Chunk from given chunk space
     */
    public float getChunkLightValue(int x, int y, int z)
    {
        return lightValues[x][y][z];
    }

    /**
     * Sets light value in Chunk from given chunk space
     */
    public void setChunkLightValue(int x, int y, int z, float lightValue)
    {
        lightValues[x][y][z] = lightValue;
        markDirty();
        markNeighbors(x, y, z);
    }

    private void markNeighbors(int x, int y, int z)
    {
        Chunk c = owner.getChunkProvider().get(owner, coords.x - 1, coords.y, coords.z);
        if(c != null)
            c.markDirty();

        c = owner.getChunkProvider().get(owner, coords.x + 1, coords.y, coords.z);
        if(c != null)
            c.markDirty();

        c = owner.getChunkProvider().get(owner, coords.x, coords.y - 1, coords.z);
        if(c != null)
            c.markDirty();

        c = owner.getChunkProvider().get(owner, coords.x, coords.y + 1, coords.z);
        if(c != null)
            c.markDirty();

        c = owner.getChunkProvider().get(owner, coords.x, coords.y, coords.z - 1);
        if(c != null)
            c.markDirty();

        c = owner.getChunkProvider().get(owner, coords.x, coords.y, coords.z + 1);
        if(c != null)
            c.markDirty();
    }

    /**
     * Returns block in Chunk from given chunk space
     */
    public Block getChunkBlock(int x, int y, int z)
    {
        Block b = Blocks.getByID(blocks[x][y][z]);
        if(b == null)
            return Blocks.air;
        return b;
    }

    /**
     * Sets block in Chunk from given chunk space
     */
    public void setChunkBlock(int x, int y, int z, Block block)
    {
        if(block == null)
            block = Blocks.air;
        else
            blocks[x][y][z] = block.getUniqueID();

        if(y >= highest[x][z])
        {
            if(block != Blocks.air)
            {
                highest[x][z] = y;
            }
            else if(y == highest[x][z])
            {
                for(; y >= 0; --y)
                {
                    if(getChunkBlock(x, y, z) != Blocks.air)
                    {
                        break;
                    }
                }
                highest[x][z] = y;
            }
        }
        markDirty();
        markNeighbors(x, y, z);
    }

    /**
     * Returns highest block in Chunk from given chunk space
     */
    public Block getHighestBlock(int x, int z)
    {
        if(highest[x][z] < 0)
            return null;
        return getChunkBlock(x, highest[x][z], z);
    }

    /**
     * Returns highest y value in Chunk from given chunk space
     */
    public int getHighest(int x, int z)
    {
        return highest[x][z];
    }

    /**
     * Sets a block state at given value from coords in world space
     */
    public void setBlockState(int worldX, int worldY, int worldZ, BlockState state, IBlockStateValue value)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0)
            x = 16 + x;
        if(y < 0)
            y = 16 + y;
        if(z < 0)
            z = 16 + z;
        setChunkBlockState(x, y, z, state, value);
    }

    /**
     * Sets a block state at given value from coords in chunk space
     */
    public void setChunkBlockState(int x, int y, int z, BlockState state, IBlockStateValue value)
    {
        getBlockStateObject(x, y, z, true).set(state, value);
        markNeighbors(x, y, z);
        markDirty();
    }

    /**
     * Returns requested block state value from world space
     */
    public IBlockStateValue getBlockState(int worldX, int worldY, int worldZ, BlockState state)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0)
            x = 16 + x;
        if(y < 0)
            y = 16 + y;
        if(z < 0)
            z = 16 + z;
        return getChunkBlockState(x, y, z, state);
    }

    /**
     * Returns requested block state value from chunk space
     */
    private IBlockStateValue getChunkBlockState(int x, int y, int z, BlockState state)
    {
        BlockStatesObject o = getBlockStateObject(x, y, z, false);
        if(o != null)
        {
            return o.get(state);
        }
        return null;
    }

    public void clearStates(int worldX, int worldY, int worldZ)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0)
            x = 16 + x;
        if(y < 0)
            y = 16 + y;
        if(z < 0)
            z = 16 + z;
        clearChunkState(x, y, z);
    }

    public void clearChunkState(int x, int y, int z)
    {
        BlockStatesObject o = getBlockStateObject(x, y, z, false);
        if(o != null)
        {
            o.clear();
            Vector3 v = Vector3.get(x, y, z);
            blockStatesObjects.put(v, null);
        }
    }

    public void update()
    {
        for(int x = 0; x < 16; x++ )
        {
            for(int z = 0; z < 16; z++ )
            {
                int maxY = (int) Math.round(4f * MathHelper.perlinNoise(x + this.getCoords().x * 16, z + this.getCoords().z * 16, owner.getGenerator().getSeed())) - (this.getCoords().y - 11) * 16 + 1;
                for(int y = 0; y <= maxY && y < 16; y++ )
                {
                    Block b = Blocks.getByID(blocks[x][y][z]);
                    if(b != null)
                        b.updateTick(owner, x, y, z);
                }
            }
        }
    }

    public BlockStatesObject getBlockStates(int worldX, int worldY, int worldZ)
    {
        int x = worldX % 16;
        int y = worldY % 16;
        int z = worldZ % 16;

        if(x < 0)
            x = 16 + x;
        if(y < 0)
            y = 16 + y;
        if(z < 0)
            z = 16 + z;
        return getBlockStateObject(x, y, z, false);
    }

    private BlockStatesObject getBlockStateObject(int x, int y, int z, boolean forceGen)
    {
        Vector3 v = Vector3.get(x, y, z);
        BlockStatesObject object = blockStatesObjects.get(v);
        if(object == null && forceGen)
        {
            blockStatesObjects.put(v, new BlockStatesObject());
            object = blockStatesObjects.get(v);
        }
        return object;
    }

    @Override
    public Collection<Entity> getEntities()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector3i getPosition()
    {
        return new Vec3i(coords.x, coords.y, coords.z);
    }

    @Override
    public Optional<Entity> createEntity(EntityType type, Vector3d position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Entity> createEntity(EntitySnapshot snapshot, Vector3d position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Biome getBiome(Vector3d position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public org.spongepowered.api.block.Block getBlock(Vector3d position)
    {
        return getBlock((int) position.getX(), (int) position.getY(), (int) position.getZ());
    }

    @Override
    public org.spongepowered.api.block.Block getBlock(int x, int y, int z)
    {
        return getBlock(x, y, z);
    }
}
