package org.craft.client;

import org.craft.client.render.*;
import org.craft.world.*;

public class ThreadGetChunksFromCamera extends Thread
{

    private OurCraft game;

    public ThreadGetChunksFromCamera(OurCraft game)
    {
        this.game = game;
        setDaemon(true);
    }

    public void run()
    {
        try
        {
            RenderEngine renderEngine = game.getRenderEngine();
            int renderDistance = 6;
            while(game.isRunning())
            {
                World clientWorld = game.getClientWorld();
                int ox = (int) renderEngine.getRenderViewEntity().getX();
                int oy = (int) renderEngine.getRenderViewEntity().getY();
                int oz = (int) renderEngine.getRenderViewEntity().getZ();
                for(int radius = 0; radius < renderDistance; radius++ )
                {
                    for(int x = -radius; x < radius; x++ )
                    {
                        yLoop: for(int y = -radius; y < radius; y++ )
                        {
                            for(int z = -radius; z < radius; z++ )
                            {
                                int fx = x * 16 + ox;
                                int fy = y * 16 + oy;
                                int fz = z * 16 + oz;

                                if(fy < 0)
                                    continue yLoop;
                                if(clientWorld != null)
                                    synchronized(clientWorld)
                                    {
                                        if(!clientWorld.doesChunkExists((int) Math.floor(fx / 16f), (int) Math.floor(fy / 16f), (int) Math.floor(fz / 16f)))
                                            clientWorld.createChunk((int) Math.floor(fx / 16f), (int) Math.floor(fy / 16f), (int) Math.floor(fz / 16f));
                                    }
                            }
                        }
                    }
                }
                try
                {
                    Thread.sleep(250);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
