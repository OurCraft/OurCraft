package org.craft.network;

import io.netty.buffer.*;

public class NettyPacket
{

    ByteBuf payload;
    int     id;

    NettyPacket()
    {

    }

    public NettyPacket(int id, ByteBuf payload)
    {
        this.id = id;
        this.payload = payload;
    }

    public int getID()
    {
        return id;
    }

    public ByteBuf getPayload()
    {
        return payload;
    }
}
