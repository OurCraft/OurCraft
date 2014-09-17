package org.craft.world;

import java.util.*;
import java.util.concurrent.*;

import org.craft.blocks.*;
import org.craft.entity.*;
import org.craft.maths.*;

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
}
