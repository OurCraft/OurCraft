package org.craft.client;

import java.util.*;
import java.util.concurrent.*;

import org.craft.maths.*;
import org.craft.world.*;

public class ChunkGeneratorTask implements Runnable
{

    private OurCraft       theGame;
    private volatile World world;
    private Queue<Vector3> genQueue;
    private Thread         thread;

    public ChunkGeneratorTask(OurCraft game)
    {
        this.theGame = game;
        this.resetThread();
        this.genQueue = new ConcurrentLinkedQueue<Vector3>();
    }

    public void resetThread()
    {
        this.thread = new Thread(this, "ChunkGenerator");
    }

    @Override
    public void run()
    {
        //System.out.println(genQueue.size());
        while(!genQueue.isEmpty())
        {
            if(world == null)
                break;
            final Vector3 vec = genQueue.poll();
            final int x = (int) vec.getX();
            final int y = (int) vec.getY();
            final int z = (int) vec.getZ();
            vec.dispose();
            world.createChunk(x, y, z);
        }
    }

    public void addToQueue(int x, int y, int z)
    {
        this.world = theGame.getClientWorld();
        if(world == null)
            return;
        final Vector3 vec = Vector3.get(x, y, z);
        if(!world.doesChunkExists(x, y, z))
        {
            boolean needed = true;
            for(Vector3 entry : genQueue)
            {
                if(entry.equals(vec))
                {
                    //Log.debug("REJECTED " + vec);
                    vec.dispose();
                    needed = false;
                    break;
                }
            }

            if(needed)
            {
                genQueue.add(vec);
            }

        }

    }

    public void execute()
    {
        if(!thread.isAlive())
        {
            this.resetThread();
            thread.start();
        }
    }

}
