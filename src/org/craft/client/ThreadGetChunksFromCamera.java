package org.craft.client;

import org.craft.client.render.*;
import org.craft.world.*;

public class ThreadGetChunksFromCamera extends Thread
{

    private OurCraft game;

    public ThreadGetChunksFromCamera(OurCraft game)
    {
        this.game = game;
    }

    public void run()
    {
        World clientWorld = game.getClientWorld();
        RenderEngine renderEngine = game.getRenderEngine();
        int renderDistance = 8;
        while(game.isRunning())
        {
            int ox = (int)renderEngine.getRenderViewEntity().getPos().x;
            int oy = (int)renderEngine.getRenderViewEntity().getPos().y;
            int oz = (int)renderEngine.getRenderViewEntity().getPos().z;
            for(int x = -renderDistance; x < renderDistance; x++ )
            {
                for(int y = -renderDistance; y < renderDistance; y++ )
                {
                    if(y < 0) continue;
                    for(int z = -renderDistance; z < renderDistance; z++ )
                    {
                        int fx = x * 16 + ox;
                        int fy = y * 16 + oy;
                        int fz = z * 16 + oz;

                        clientWorld.getChunkProvider().getOrCreate(clientWorld, (int)Math.floor((float)fx / 16f), (int)Math.floor((float)fy / 16f), (int)Math.floor((float)fz / 16f));
                    }
                }
            }
            try
            {
                Thread.sleep(1);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
