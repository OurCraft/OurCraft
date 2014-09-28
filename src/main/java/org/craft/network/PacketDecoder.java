package org.craft.network;

import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.handler.codec.*;

import java.util.*;

public class PacketDecoder extends ByteToMessageDecoder
{

    private ByteBuf     buffer;
    private int         waitingPayloadSize;
    private NettyPacket packet;

    public PacketDecoder()
    {
        waitingPayloadSize = -1;
        buffer = Unpooled.buffer();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf data, List<Object> out) throws Exception
    {
        buffer.writeBytes(data);
        if(waitingPayloadSize >= 0)
        {
            if(buffer.readableBytes() < waitingPayloadSize)
                return;
            ByteBuf payload = buffer.readBytes(Math.min(buffer.readableBytes(), waitingPayloadSize));
            ByteBuf payloadData = payload.readBytes(waitingPayloadSize);
            packet.payload = payloadData;
            out.add(packet);
            packet = null;
            waitingPayloadSize = -1;
            if(payload.readableBytes() > 0)
                decode(ctx, payload, out);
        }
        else
        {
            if(buffer.readableBytes() >= 8)
            {
                packet = new NettyPacket();
                packet.id = buffer.readInt();
                int payloadSize = buffer.readInt();
                if(buffer.readableBytes() < payloadSize)
                {
                    waitingPayloadSize = payloadSize;
                }
                else
                {
                    ByteBuf payload = buffer.readBytes(payloadSize);
                    packet.payload = payload;
                    out.add(packet);
                }
            }
        }
    }
}
