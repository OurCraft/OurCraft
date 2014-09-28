package org.craft.world;

import java.util.*;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.spongepowered.api.math.*;
import org.spongepowered.api.world.*;

public class World implements org.spongepowered.api.world.World
{

    private LinkedList<Entity> entities;
    private ArrayList<Entity>  spawingQueue;
    private ChunkProvider      chunkProvider;
    private WorldGenerator     generator;
    private String             name;

    public World(String name, ChunkProvider prov, WorldGenerator generator)
    {
        this.name = name;
        this.generator = generator;
        this.chunkProvider = prov;
        spawingQueue = new ArrayList<Entity>();
        entities = new LinkedList<Entity>();
    }

    public void update(int time, boolean canUpdate)
    {
        while(!spawingQueue.isEmpty())
            entities.add(spawingQueue.remove(0));
        ArrayList<Entity> deadEntities = new ArrayList<Entity>();
        if(canUpdate)
        {
            for(Entity e : entities)
            {
                Chunk c = getChunk((int) e.getX(), (int) e.getY(), (int) e.getZ());
                if(c != null)
                    c.update();
                e.update();

                if(e.isDead())
                {
                    deadEntities.add(e);
                }
            }
        }
        entities.removeAll(deadEntities);

    }

    /**
     * Returns chunk from given coords in world space
     */
    public Chunk getChunk(int x, int y, int z)
    {
        return chunkProvider.get(this, (int) Math.floor((float) x / 16f), (int) Math.floor((float) y / 16f), (int) Math.floor((float) z / 16f));
    }

    public void addChunk(Chunk c)
    {
        chunkProvider.addChunk(this, c);
    }

    /**
     * Returns block next to given coords and given side
     */
    public Block getBlockNextTo(int x, int y, int z, EnumSide side)
    {
        return getBlock(x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ());
    }

    /**
     * Returns block at given coords
     */
    public Block getBlock(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
            return Blocks.air;
        return c.getBlock(this, x, y, z);
    }

    public IBlockStateValue getBlockState(int x, int y, int z, BlockState state)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
            return null;
        return c.getBlockState(x, y, z, state);
    }

    /**
     * Sets block state at given coords
     */
    public void setBlockState(int x, int y, int z, BlockState state, IBlockStateValue value)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
            return;
        c.setBlockState(x, y, z, state, value);
    }

    /**
     * Sets block at given coords
     */
    public void setBlock(int x, int y, int z, Block block)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
        {
            return;
        }
        c.setBlock(this, x, y, z, block);
        c.markDirty();
    }

    /**
     * Spawns a new entity in world
     */
    public void spawn(Entity e)
    {
        Log.message("added " + e.getClass());
        this.spawingQueue.add(e);
    }

    /**
     * Performs a raycast from {@code sender} to get object in front of it. Results are saved into {@code infos}
     */
    public void performRayCast(Entity sender, CollisionInfos infos, float maxDist)
    {
        float maxReachedDist = maxDist; // Thog, DO NOT REMOVE THIS LINE
        float size = 0.45f;
        Vector3 origin = Vector3.get(sender.getX(), sender.getY() + sender.getEyeOffset() - size / 2f, sender.getZ());
        Vector3 pos = origin;
        Vector3 ray = sender.getQuaternionRotation().getForward();

        int x = 0;
        int y = 0;
        int z = 0;
        float step = 0.005f;
        AABB rayBB = new AABB(Vector3.get(0, 0, 0), Vector3.get(size, size, size));
        Vector3 blockPos = Vector3.get(x, y, z);
        for(float dist = 0f; dist <= maxDist + step; dist += step)
        {
            x = (int) Math.round(pos.getX());
            y = (int) Math.round(pos.getY());
            z = (int) Math.round(pos.getZ());
            Block b = getBlock(x, y, z);
            if(b != null)
            {
                AABB blockBB = b.getSelectionBox(this, x, y, z);
                if(blockBB != null)
                {
                    if(blockBB.intersectAABB(rayBB.translate(pos)).doesIntersects())
                    {
                        infos.x = x;
                        infos.y = y;
                        infos.z = z;
                        infos.type = CollisionType.BLOCK;
                        infos.side = EnumSide.BOTTOM;
                        infos.value = b;
                        blockPos = Vector3.get(x + 0.5f, y + 0.5f, z + 0.5f);

                        Vector3 diff = blockPos.sub(pos.add(0.5f));

                        float absx = Math.abs(diff.getX());
                        float absy = Math.abs(diff.getY());
                        float absz = Math.abs(diff.getZ());

                        if(absx > absy && absx > absz)
                        {
                            if(diff.getX() > 0)
                                infos.side = EnumSide.WEST;
                            else
                                infos.side = EnumSide.EAST;
                        }
                        if(absy > absx && absy > absz)
                        {
                            if(diff.getY() > 0)
                                infos.side = EnumSide.BOTTOM;
                            else
                                infos.side = EnumSide.TOP;
                        }
                        if(absz > absy && absz > absx)
                        {
                            if(diff.getZ() > 0)
                                infos.side = EnumSide.NORTH;
                            else
                                infos.side = EnumSide.SOUTH;
                        }
                    }
                }
            }
            pos = origin.add(ray.mul((maxDist + step) - dist));
            for(Entity e : entities)
            {
                if(e.getBoundingBox().intersectAABB(rayBB).doesIntersects())
                {
                    infos.type = CollisionType.ENTITY;
                    infos.value = e;
                    infos.x = e.getX();
                    infos.y = e.getY();
                    infos.z = e.getZ();
                    infos.distance = maxReachedDist;
                }
            }
        }
    }

    public WorldGenerator getGenerator()
    {
        return generator;
    }

    public ChunkProvider getChunkProvider()
    {
        return chunkProvider;
    }

    /**
     * Returns true if given block can see the sky
     */
    public boolean canBlockSeeSky(int x, int y, int z)
    {
        for(int y1 = y + 1; y1 < 256; y1++ )
        {
            if(!getBlock(x, y1, z).letLightGoThrough())
            {
                return false;
            }
        }
        return true;
    }

    public List<Entity> getEntitiesList()
    {
        return entities;
    }

    public void clearStates(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
        {
            return;
        }
        c.clearStates(x, y, z);
    }

    /**
     * Returns whether a chunk exists at chunk coordinates x, y, z
     */
    public boolean doesChunkExists(int x, int y, int z)
    {
        return this.chunkProvider.doesChunkExists(this, x, y, z);
    }

    public void createChunk(final int x, final int y, final int z)
    {
        final World w = this;
        if(doesChunkExists(x, y, z))
            Log.error("Cannot generate a chunk on a chunk on " + x + ", " + y + ", " + z);
        else
        {
            Thread t = new Thread()
            {
                @Override
                public void run()
                {
                    chunkProvider.create(w, x, y, z);
                }
            };
            t.start();
            try
            {
                t.join();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }

    public BlockStatesObject getBlockStates(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c != null)
        {
            return c.getBlockStates(x, y, z);
        }
        return null;
    }

    @Override
    public Location at(Vector2d position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location at(int x, int y, int z)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public org.spongepowered.api.block.Block getBlock(Vector3i position)
    {
        return getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public boolean setBlock(Vector3i position, org.spongepowered.api.block.Block block)
    {
        setBlock(position.getX(), position.getY(), position.getZ(), (Block) block);
        return true;
    }

    @Override
    public byte getLuminance(Vector3i position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getLuminanceFromSky(Vector3i position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getLuminanceFromGround(Vector3i position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Collection<org.spongepowered.api.entity.Entity> getEntities()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UUID getUniqueID()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public org.spongepowered.api.world.Chunk getChunk(Vector2i position)
    {
        return getChunk(position.getX(), 0, position.getY());
    }

    @Override
    public org.spongepowered.api.world.Chunk loadChunk(Vector2i position, boolean shouldGenerate)
    {
        if(shouldGenerate)
            return chunkProvider.getOrCreate(this, position.getX(), 0, position.getY());
        else
            return getChunk(position);
    }

    @Override
    public org.spongepowered.api.world.Chunk loadChunk(Vector2i position)
    {
        return loadChunk(position, false);
    }
}
