package org.craft.network;

import io.netty.buffer.*;
import io.netty.channel.*;

public abstract class ChannelHandler extends ChannelInboundHandlerAdapter
{

    protected INetworkHandler netHandler;

    public ChannelHandler(INetworkHandler netHandler)
    {
        this.netHandler = netHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        NettyPacket m = (NettyPacket) msg;
        try
        {
            AbstractPacket packet = PacketRegistry.create(m.getSide(), m.getID());
            packet.decodeFrom(m.getPayload());
            netHandler.handlePacket(packet);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void channelActive(ChannelHandlerContext ctx)
    {
        netHandler.onConnexionEstablished(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }

    protected void write(AbstractPacket packet, ChannelHandlerContext ctx)
    {
        int id = PacketRegistry.getPacketId(packet.getClass());
        NetworkSide side = PacketRegistry.getPacketSide(packet.getClass());
        ByteBuf buffer = ctx.alloc().buffer();
        packet.encodeInto(buffer);
        NettyPacket nettyPacket = new NettyPacket(id, buffer, side);
        ctx.writeAndFlush(nettyPacket);
    }
}
