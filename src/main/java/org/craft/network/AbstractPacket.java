package org.craft.network;

import io.netty.buffer.*;

public abstract class AbstractPacket
{

    public AbstractPacket()
    {

    }

    public abstract void decodeFrom(ByteBuf buffer);

    public abstract void encodeInto(ByteBuf buffer);
}
