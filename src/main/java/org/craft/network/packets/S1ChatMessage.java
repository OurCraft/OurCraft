package org.craft.network.packets;

import io.netty.buffer.*;

import org.craft.network.*;

public class S1ChatMessage extends AbstractPacket
{

    private String message;

    public S1ChatMessage()
    {

    }

    public S1ChatMessage(String message)
    {
        this.message = message;
    }

    @Override
    public void decodeFrom(ByteBuf buffer)
    {
        message = ByteBufUtils.readString(buffer);
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        ByteBufUtils.writeString(buffer, message);
    }

    public String getMessage()
    {
        return message;
    }

}
