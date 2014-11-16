package org.craft.world;

import java.util.*;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;

public class World
{
    private LinkedList<Entity> entities;
    private ArrayList<Entity>  spawingQueue;
    private ChunkProvider      chunkProvider;
    private WorldGenerator     generator;
    private String             name;
    private WorldLoader        worldLoader;
    public boolean             isRemote;
    private Random             rng;
    private long               tick;
    private float              gravity;

    public World(String name, ChunkProvider prov, WorldGenerator generator, WorldLoader worldLoader)
    {
        this.rng = new Random(generator.getSeed());
        this.worldLoader = worldLoader;
        this.name = name;
        this.generator = generator;
        this.chunkProvider = prov;
        this.gravity = 9.81f / 360f;
        spawingQueue = new ArrayList<Entity>();
        entities = new LinkedList<Entity>();
    }

    public void update(double delta)
    {
        while(!spawingQueue.isEmpty())
            entities.add(spawingQueue.remove(0));
        ArrayList<Entity> deadEntities = new ArrayList<Entity>();
        for(Entity e : entities)
        {
            Chunk c = getChunk((int) e.getX(), (int) e.getY(), (int) e.getZ());
            if(c != null)
                c.update();
            e.update(delta);

            if(e.isDead())
            {
                deadEntities.add(e);
            }
        }
        entities.removeAll(deadEntities);
        tick++ ;

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
        return getBlockAt(x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ());
    }

    /**
     * Returns block at given coords
     */
    public Block getBlockAt(int x, int y, int z)
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
        setBlockState(x, y, z, state, value, false);
    }

    public void setBlockState(int x, int y, int z, BlockState state, IBlockStateValue value, boolean notify)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
            return;
        c.setBlockState(x, y, z, state, value);
        if(notify)
            updateBlockNeighbors(x, y, z, false);
    }

    /**
     * Sets block at given coords
     */
    public void setBlock(int x, int y, int z, Block block)
    {
        setBlock(x, y, z, block, true);
    }

    public void setBlock(int x, int y, int z, Block block, boolean notify)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
        {
            return;
        }
        c.setBlock(this, x, y, z, block);
        c.markDirty();
        if(notify)
            updateBlockNeighbors(x, y, z, false);
    }

    /**
     * Spawns a new entity in world
     */
    public void spawn(Entity e)
    {
        this.spawingQueue.add(e);
    }

    /**
     * Performs a raycast from {@code sender} to get object in front of it. Results are saved into {@code infos}
     */
    public void performRayCast(Entity sender, CollisionInfos infos, float maxDist)
    {
        float maxReachedDist = maxDist;
        float size = 0.45f;
        Vector3 origin = Vector3.get(sender.posX, sender.posY + sender.getEyeOffset() - size / 2f, sender.posZ);
        Vector3 pos = origin;
        Vector3 ray = sender.getQuaternionRotation().getForward();

        int x = 0;
        int y = 0;
        int z = 0;
        float step = 0.005f;
        AABB rayBB = new AABB(Vector3.get(0, 0, 0), Vector3.get(size, size, size));
        Vector3 blockPos = null;
        for(float dist = 0f; dist <= maxDist + step; dist += step)
        {
            x = (int) Math.round(pos.getX());
            y = (int) Math.round(pos.getY());
            z = (int) Math.round(pos.getZ());
            Block b = getBlockAt(x, y, z);
            if(b != null)
            {
                AABB blockBB = b.getSelectionBox(this, x, y, z);
                if(blockBB != null)
                {
                    AABB translatedRay = rayBB.translate(pos);
                    if(blockBB.intersectAABB(translatedRay))
                    {
                        infos.x = x;
                        infos.y = y;
                        infos.z = z;
                        infos.type = CollisionType.BLOCK;
                        infos.side = EnumSide.BOTTOM;
                        infos.value = b;
                        blockPos = Vector3.get(x + 0.5f, y + 0.5f, z + 0.5f);

                        Vector3 diff = blockPos.sub(pos.add(0.5f));
                        blockPos.dispose();

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
                        diff.dispose();
                    }
                    translatedRay.dispose();
                    blockBB.dispose();
                }
            }
            if(pos != origin)
                pos.dispose();
            Vector3 multipliedRay = ray.mul((maxDist + step) - dist);
            pos = origin.add(multipliedRay);
            multipliedRay.dispose();
            for(Entity e : entities)
            {
                if(e.getBoundingBox().intersectAABB(rayBB))
                {
                    infos.type = CollisionType.ENTITY;
                    infos.value = e;
                    infos.x = e.posX;
                    infos.y = e.posY;
                    infos.z = e.posZ;
                    infos.distance = maxReachedDist;
                }
            }
        }
        if(blockPos != null)
            blockPos.dispose();
        origin.dispose();
        pos.dispose();
        ray.dispose();
        rayBB.dispose();
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
            if(!getBlockAt(x, y1, z).letLightGoThrough())
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
        //Log.message("Generating chunk on " + x + ", " + y + ", " + z);
        if(doesChunkExists(x, y, z))
            Log.error("Cannot generate a chunk on a chunk on " + x + ", " + y + ", " + z);
        else
        {
            chunkProvider.create(this, x, y, z);
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

    public boolean updateBlock(int x, int y, int z, boolean force, ArrayList<Vector3> visited)
    {
        boolean disposeList = false;
        if(visited == null)
        {
            disposeList = true;
            visited = new ArrayList<Vector3>();
        }
        Block b = getBlockAt(x, y, z);
        if(b != null)
        {
            Vector3 posVec = Vector3.get(x, y, z);
            if(!visited.contains(posVec) || force)
            {
                visited.add(Vector3.get(x, y, z));
                b.onBlockUpdate(this, x, y, z, visited);
            }
            posVec.dispose();
            return true;
        }
        if(disposeList)
        {
            for(Vector3 v : visited)
                v.dispose();
            visited.clear();
        }
        return false;
    }

    public void cleanDirtiness(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c != null)
            c.cleanDirtiness(x, y, z);
    }

    public boolean isDirty(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c != null)
            return c.isDirty(x, y, z);
        return false;
    }

    protected boolean updateBlockFromNeighbor(int x, int y, int z, boolean force, ArrayList<Vector3> visited)
    {
        Block b = getBlockAt(x, y, z);
        if(b != null)
        {
            Vector3 posVec = Vector3.get(x, y, z);
            if(!visited.contains(posVec) || force)
            {
                visited.add(Vector3.get(x, y, z));
                b.onBlockUpdateFromNeighbor(this, x, y, z, visited);
            }
            posVec.dispose();
            return true;
        }
        return false;
    }

    public void updateBlockAndNeighbors(int x, int y, int z, boolean force, ArrayList<Vector3> visited)
    {
        boolean disposeList = false;
        if(visited == null)
        {
            disposeList = true;
            visited = new ArrayList<Vector3>();
        }
        updateBlock(x, y, z, force, visited);
        updateBlockNeighbors(x, y, z, force, visited);
        if(disposeList)
        {
            for(Vector3 v : visited)
                v.dispose();
            visited.clear();
        }
    }

    public void updateBlockNeighbors(int x, int y, int z, boolean force)
    {
        updateBlockNeighbors(x, y, z, force, null);
    }

    public void updateBlockNeighbors(int x, int y, int z, boolean force, ArrayList<Vector3> visited)
    {
        boolean disposeList = false;
        if(visited == null)
        {
            visited = new ArrayList<Vector3>();
            disposeList = true;
        }
        updateBlockFromNeighbor(x, y, z + 1, force, visited);
        updateBlockFromNeighbor(x, y, z - 1, force, visited);
        updateBlockFromNeighbor(x, y + 1, z, force, visited);
        updateBlockFromNeighbor(x, y - 1, z, force, visited);
        updateBlockFromNeighbor(x + 1, y, z, force, visited);
        updateBlockFromNeighbor(x - 1, y, z, force, visited);
        if(disposeList)
        {
            for(Vector3 v : visited)
                v.dispose();
            visited.clear();
        }

    }

    public int getDirectElectricPowerAt(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
            return 0;
        int maxPower = 0;
        for(EnumSide side : EnumSide.values())
        {
            if(side == EnumSide.UNDEFINED)
                continue;
            IBlockStateValue value = getBlockState(x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), BlockStates.electricPower);
            if(value != null && value instanceof EnumPowerStates)
            {
                EnumPowerStates power = (EnumPowerStates) value;
                if(power.powerValue() == 15)
                    return 15;
                else if(power.powerValue() > maxPower)
                    maxPower = power.powerValue();
            }
        }
        return maxPower;
    }

    public String getName()
    {
        return name;
    }

    public void setSeed(long seed)
    {
        this.getGenerator().setSeed(seed);
    }

    public long getSeed()
    {
        return getGenerator().getSeed();
    }

    public WorldLoader getLoader()
    {
        return worldLoader;
    }

    public Random getRNG()
    {
        return rng;
    }

    public long getTick()
    {
        return tick;
    }

    public float getGravity()
    {
        return gravity;
    }
}
