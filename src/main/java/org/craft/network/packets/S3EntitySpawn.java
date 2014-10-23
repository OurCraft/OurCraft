package org.craft.network.packets;

import io.netty.buffer.*;

import org.craft.entity.*;
import org.craft.network.*;

public class S3EntitySpawn extends AbstractPacket
{

    private Entity entity;

    public S3EntitySpawn()
    {
        ;
    }

    public S3EntitySpawn(Entity e)
    {
        this.entity = e;
    }

    @Override
    public void decodeFrom(ByteBuf buffer)
    {

    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        // TODO Auto-generated method stub

    }

}
