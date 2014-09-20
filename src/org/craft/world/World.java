package org.craft.world;

import java.util.*;
import java.util.concurrent.*;

import org.craft.blocks.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.util.*;
import org.craft.util.CollisionInfos.CollisionType;
import org.craft.utils.*;

public class World
{

    private LinkedList<Entity>          entities;
    private LinkedBlockingQueue<Entity> spawingQueue;
    private AABB                        groundBB;
    private ChunkProvider               chunkProvider;
    private WorldGenerator              generator;

    public World(ChunkProvider prov, WorldGenerator generator)
    {
        this.generator = generator;
        this.chunkProvider = prov;
        spawingQueue = new LinkedBlockingQueue<>();
        entities = new LinkedList<>();
        groundBB = new AABB(Vector3.get(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY), Vector3.get(Float.POSITIVE_INFINITY, 0, Float.POSITIVE_INFINITY));
    }

    public void update()
    {
        while(!spawingQueue.isEmpty())
            entities.add(spawingQueue.poll());
        ArrayList<Entity> deadEntities = new ArrayList<>();
        for(Entity e : entities)
        {
            e.update();

            if(e.isDead())
            {
                deadEntities.add(e);
            }
        }
        entities.removeAll(deadEntities);

    }

    public Chunk getChunk(int x, int y, int z)
    {
        return chunkProvider.get(this, (int)Math.floor((float)x / 16f), (int)Math.floor((float)y / 16f), (int)Math.floor((float)z / 16f));
    }

    public void addChunk(Chunk c)
    {
        chunkProvider.addChunk(this, c);
    }

    public Block getBlockNextTo(int x, int y, int z, EnumSide side)
    {
        return getBlock(x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ());
    }

    public Block getBlock(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null) return Blocks.air;
        return c.getBlock(this, x, y, z);
    }

    public void setBlock(int x, int y, int z, Block block)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
        {
            Log.error("Couldn't place block at " + x + "," + y + "," + z);
            return;
        }
        c.setBlock(this, x, y, z, block);
        c.markDirty();
    }

    public void spawn(Entity e)
    {
        this.spawingQueue.add(e);
    }

    public AABB getGroundBB()
    {
        return groundBB;
    }

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

        float step = 0.1f;
        Vector3 startPos = sender.getPos().add(0f, sender.getEyeOffset(), 0f);
        Vector3 look = sender.getRotation().getForward();
        Vector3 currentPos = startPos;
        AABB bb = new AABB(Vector3.get(-0.01f, -0.01f, -0.01f), Vector3.get(0.01f, 0.01f, 0.01f));
        Vector3 blockPos = Vector3.NULL.copy();
        float dist = 0f;
        while(dist < maxDist)
        {
            int x = (int)(currentPos.getX());
            int y = (int)(currentPos.getY());
            int z = (int)(currentPos.getZ());
            currentPos = startPos.add(look.mul(dist));
            dist += step;
            Block b = getBlock(x, y, z);
            if(b == null)
            {
                continue;
            }
            int dx = (int)(startPos.getX()) - x;
            int dy = (int)(startPos.getY()) - y;
            int dz = (int)(startPos.getZ()) - z;
            float blockDist = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
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
                Vector3 dir = sender.getPos().sub(blockPos);
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
                        infos.side = EnumSide.EAST;
                    }
                    else
                        infos.side = EnumSide.WEST;
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
}
