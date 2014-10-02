package org.craft.client.network;

import io.netty.channel.*;

import org.craft.network.*;
import org.craft.utils.*;

public class NettyClientChannelHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        NettyPacket m = (NettyPacket) msg;
        try
        {
            AbstractPacket packet = PacketRegistry.create(m.getSide(), m.getID());
            packet.decodeFrom(m.getPayload());

            Log.message(packet.toString());
            // TODO: handle the packet
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
