package org.craft.server.network.packets;

import io.netty.buffer.*;

import org.craft.network.*;
import org.craft.utils.*;

public class S0ConnectionAccepted extends AbstractPacket
{

    public S0ConnectionAccepted()
    {

    }

    @Override
    public void decodeFrom(ByteBuf buffer)
    {
        Log.message("DECODED: " + buffer.readInt());
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeInt(2);
    }

}
