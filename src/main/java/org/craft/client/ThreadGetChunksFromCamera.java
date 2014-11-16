package org.craft.client;

import org.craft.client.render.*;
import org.craft.utils.Log;
import org.craft.world.*;

public class ThreadGetChunksFromCamera extends Thread implements Runnable
{

    private OurCraft game;

    public ThreadGetChunksFromCamera(OurCraft game)
    {
        this.setName("ChunksFromCamera");
        this.game = game;
        setDaemon(true);
    }

    public void run()
    {
        
        try
        {
            RenderEngine renderEngine = game.getRenderEngine();
            ChunkGeneratorTask gen = new ChunkGeneratorTask(game);
            int renderDistance = 6;
            System.out.println(Thread.currentThread().getName());
            while(game.isRunning())
            {
                
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
                                gen.addToQueue((int) Math.floor(fx / 16f), (int) Math.floor(fy / 16f), (int) Math.floor(fz / 16f));
                            }
                        }
                    }
                }
                gen.execute();
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
