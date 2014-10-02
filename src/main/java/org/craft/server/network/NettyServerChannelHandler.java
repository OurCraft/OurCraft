package org.craft.server.network;

import io.netty.channel.*;

import org.craft.network.*;
import org.craft.server.network.packets.*;

public class NettyServerChannelHandler extends ChannelInboundHandlerAdapter
{

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg)
    {
        NettyPacket receivedPacket = (NettyPacket) msg;
        try
        {
            AbstractPacket packet = PacketRegistry.create(receivedPacket.getSide(), receivedPacket.getID());
            packet.decodeFrom(receivedPacket.getPayload());

            // TODO: handle the packet
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx)
    {
        ctx.writeAndFlush(new S0ConnectionAccepted());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
