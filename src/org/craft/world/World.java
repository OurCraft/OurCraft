package org.craft.world;

import java.util.*;
import java.util.concurrent.*;

import org.craft.blocks.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.util.*;
import org.craft.util.CollisionInfos.CollisionType;

public class World
{

    private ChunkMap                    chunkMap;
    private LinkedList<Entity>          entities;
    private LinkedBlockingQueue<Entity> spawingQueue;
    private AABB                        groundBB;

    public World()
    {
        spawingQueue = new LinkedBlockingQueue<>();
        this.chunkMap = new ChunkMap();
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

        float step = 0.4f;
        Vector3 startPos = sender.getPos().add(0.5f, sender.getEyeOffset() + 0.5f, 0.5f);
        Vector3 look = sender.getRotation().getForward().normalize();
        Vector3 currentPos = startPos;
        AABB bb = new AABB(Vector3.get(-1f, -1f, -1f), Vector3.get(1, 1, 1));
        int x = (int)Math.floor(currentPos.getX());
        int y = (int)Math.round(currentPos.getY());
        int z = (int)Math.floor(currentPos.getZ());
        int lastX = -10000 + x; // in order not to make the algorithm skip the
                                // first
                                // case, and so every case after
        int lastY = -10000 + y;
        int lastZ = -10000 + z;
        Vector3 blockPos = Vector3.NULL.copy();
        for(float dist = 0; dist <= maxDist + step; dist += step)
        {
            currentPos = startPos.add(look.mul(dist));

            x = (int)Math.floor(currentPos.getX());
            y = (int)Math.round(currentPos.getY());
            z = (int)Math.floor(currentPos.getZ());
            if(x == lastX && y == lastY && z == lastZ)
            {
                continue;
            }
            lastX = x;
            lastY = y;
            lastZ = z;
            Block b = getBlock(x, y, z);
            if(b == null) continue;
            float dx = currentPos.getX() - x;
            float dy = currentPos.getY() - y;
            float dz = currentPos.getZ() - z;
            float blockDist = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
            blockPos.set(x, y, z);
            AABB blockBB = b.getSelectionBox(this, x, y, z);
            if(blockBB == null) continue;
            if(blockDist < maxReachedDist && blockBB.intersectAABB(bb.translate(currentPos)).doesIntersects())
            {
                maxReachedDist = blockDist;
                infos.type = CollisionType.BLOCK;
                Vector3 dir = blockPos.sub(sender.getPos());
                infos.side = EnumSide.BOTTOM;
                infos.distance = maxReachedDist;
                infos.value = b;
                infos.x = x;
                infos.y = y;
                infos.z = z;
            }
        }
    }
}
