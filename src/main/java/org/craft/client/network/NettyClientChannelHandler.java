package org.craft.client.network;

import io.netty.channel.*;

import org.craft.network.*;

public class NettyClientChannelHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        NettyPacket m = (NettyPacket) msg;
        try
        {
            AbstractPacket packet = PacketRegistry.create(m.getID());
            packet.decodeFrom(m.getPayload());

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
