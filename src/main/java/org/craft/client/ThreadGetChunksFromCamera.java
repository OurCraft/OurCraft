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
            World clientWorld = game.getClientWorld();
            RenderEngine renderEngine = game.getRenderEngine();
            int renderDistance = 6;
            while(game.isRunning())
            {
                int ox = (int) renderEngine.getRenderViewEntity().getX();
                int oy = (int) renderEngine.getRenderViewEntity().getY();
                int oz = (int) renderEngine.getRenderViewEntity().getZ();
                for(int x = -renderDistance; x < renderDistance; x++ )
                {
                    yLoop: for(int y = -renderDistance; y < renderDistance; y++ )
                    {
                        for(int z = -renderDistance; z < renderDistance; z++ )
                        {
                            int fx = x * 16 + ox;
                            int fy = y * 16 + oy;
                            int fz = z * 16 + oz;

                            if(fy < 0)
                                continue yLoop;
                            synchronized(clientWorld)
                            {
                                if(!clientWorld.doesChunkExists((int) Math.floor((float) fx / 16f), (int) Math.floor((float) fy / 16f), (int) Math.floor((float) fz / 16f)))
                                    clientWorld.createChunk((int) Math.floor((float) fx / 16f), (int) Math.floor((float) fy / 16f), (int) Math.floor((float) fz / 16f));
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
