package org.craft.network.packets;

import io.netty.buffer.*;

import org.craft.network.*;

public class C2MovePlayer extends AbstractPacket
{

    private byte             direction;

    public static final byte FORWARD   = 0;
    public static final byte BACKWARDS = 1;
    public static final byte LEFT      = 2;
    public static final byte RIGHT     = 3;

    public C2MovePlayer()
    {
    }

    public C2MovePlayer(byte direction)
    {
        this.direction = direction;
    }

    public byte getDirection()
    {
        return direction;
    }

    @Override
    public void decodeFrom(ByteBuf buffer)
    {
        buffer.writeByte(direction);
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        direction = buffer.readByte();
    }

}
