package org.craft.server.network;

import io.netty.buffer.*;
import io.netty.channel.*;

import org.craft.network.*;
import org.craft.server.network.packets.*;
import org.craft.utils.*;

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

            Log.message(packet.toString());
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
        Log.message("new client");
        write(new S0ConnectionAccepted(), ctx);
    }

    private void write(AbstractPacket packet, ChannelHandlerContext ctx)
    {
        int id = PacketRegistry.getPacketId(packet.getClass());
        NetworkSide side = PacketRegistry.getPacketSide(packet.getClass());
        ByteBuf buffer = ctx.alloc().buffer();
        packet.encodeInto(buffer);
        NettyPacket nettyPacket = new NettyPacket(id, buffer, side);
        ctx.writeAndFlush(nettyPacket);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
