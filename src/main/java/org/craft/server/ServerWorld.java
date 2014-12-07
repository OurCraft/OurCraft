package org.craft.server;

import org.craft.entity.*;
import org.craft.network.packets.*;
import org.craft.world.*;

public class ServerWorld extends World
{

    public ServerWorld(String name, ChunkProvider prov, WorldGenerator generator, WorldLoader worldLoader)
    {
        super(name, prov, generator, worldLoader);
    }

    @Override
    public void spawn(Entity e)
    {
        super.spawn(e);
        OurCraftServer.getServer().getNettyWrapper().sendPacketToAll(new S3EntitySpawn(e));
    }
}
