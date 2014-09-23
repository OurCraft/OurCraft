package org.craft.world;

import java.util.*;
import java.util.concurrent.*;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;

public class World
{

    private LinkedList<Entity>          entities;
    private LinkedBlockingQueue<Entity> spawingQueue;
    private ChunkProvider               chunkProvider;
    private WorldGenerator              generator;

    public World(ChunkProvider prov, WorldGenerator generator)
    {
        this.generator = generator;
        this.chunkProvider = prov;
        spawingQueue = new LinkedBlockingQueue<Entity>();
        entities = new LinkedList<Entity>();
    }

    public void update(int time, boolean canUpdate)
    {
        while(!spawingQueue.isEmpty())
            entities.add(spawingQueue.poll());
        ArrayList<Entity> deadEntities = new ArrayList<Entity>();
        if(canUpdate)
        {
            for(Entity e : entities)
            {
                Chunk c = getChunk((int)e.getX(), (int)e.getY(), (int)e.getZ());
                if(c != null) c.update();
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
        this.spawingQueue.add(e);
    }

    /**
     * Performs a raycast from {@code sender} to get object in front of it. Results are saved into {@code infos}
     */
    public void performRayCast(Entity sender, CollisionInfos infos, float maxDist)
    {
        float maxReachedDist = Float.POSITIVE_INFINITY;
        /*
         * for(Entity e : entities)
         * {
         * if(maxReachedDist > e.dist(sender) && sender != e)
         * {
         * maxReachedDist = e.dist(sender);
         * infos.type = CollisionType.ENTITY;
         * infos.value = e;
         * infos.x = e.getPos().x;
         * infos.y = e.getPos().y;
         * infos.z = e.getPos().z;
         * infos.distance = maxReachedDist;
         * }
         * }
         */

        float step = 0.5f;
        Vector3 startPos = Vector3.get(sender.getX(), sender.getY() + sender.getEyeOffset(), sender.getZ());
        Vector3 look = sender.getRotation().getForward();
        Vector3 currentPos = startPos;
        AABB bb = new AABB(Vector3.get(-0.01f, -0.01f, -0.01f), Vector3.get(0.01f, 0.01f, 0.01f));
        Vector3 blockPos = Vector3.NULL.copy();
        float dist = 0f;
        while(dist < maxDist)
        {
            int x = (int) (currentPos.getX());
            int y = (int) (currentPos.getY());
            int z = (int) (currentPos.getZ());
            currentPos = startPos.add(look.mul(dist));
            dist += step;
            Block b = getBlock(x, y, z);
            if(b == null)
            {
                continue;
            }
            float dx = (int) (currentPos.getX()) - (x + 0.5f);
            float dy = (int) (currentPos.getY()) - (y + 0.5f);
            float dz = (int) (currentPos.getZ()) - (z + 0.5f);
            float blockDist = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
            blockPos.set(x, y, z);
            AABB blockBB = b.getSelectionBox(this, x, y, z);
            if(blockBB == null)
            {
                currentPos = currentPos.add(look.mul(step));
                continue;
            }
            if(blockDist < maxReachedDist && (blockBB.intersectAABB(bb.translate(currentPos)).doesIntersects() || blockBB.intersectAABB(bb.translate(currentPos)).getDistance() < 1f / 16f))
            {
                maxReachedDist = blockDist;
                Vector3 dir = Vector3.get(sender.getX(), sender.getY() + sender.getEyeOffset(), sender.getZ()).sub(blockPos);
                float max = Math.max(Math.abs(dir.getX()), Math.max(Math.abs(dir.getY()), Math.abs(dir.getZ())));
                if(max == Math.abs(dir.getZ()))
                {
                    if(dir.getZ() < 0)
                    {
                        infos.side = EnumSide.NORTH;
                    }
                    else
                        infos.side = EnumSide.SOUTH;
                }
                if(max == Math.abs(dir.getX()))
                {
                    if(dir.getX() < 0)
                    {
                        infos.side = EnumSide.WEST;
                    }
                    else
                        infos.side = EnumSide.EAST;
                }
                if(max == Math.abs(dir.getY()))
                {
                    if(dir.getY() < 0)
                    {
                        infos.side = EnumSide.BOTTOM;
                    }
                    else
                        infos.side = EnumSide.TOP;
                }
                infos.type = CollisionType.BLOCK;
                infos.distance = maxReachedDist;
                infos.value = b;
                infos.x = x;
                infos.y = y;
                infos.z = z;
            }
            currentPos = currentPos.add(look.mul(step));
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
    public boolean chunkExists(int x, int y, int z)
    {
        return this.chunkProvider.chunkExists(this, x, y, z);
    }

    public void createChunk(final int x, final int y, final int z)
    {
        final World w = this;
        if(chunkExists(x, y, z))
            Log.error("Cannot generate a chunk on a chunk on " + x + ", " + y + ", " + z);
        else {
            Thread t = new Thread() {
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
    }
}
