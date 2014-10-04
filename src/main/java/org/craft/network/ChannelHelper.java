package org.craft.network;

import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.util.concurrent.*;

public class ChannelHelper
{

    public static void writeAndFlush(AbstractPacket packet, ChannelHandlerContext ctx)
    {
        writeAndFlush(packet, ctx.channel());
    }

    public static void writeAndFlush(AbstractPacket packet, Channel channel)
    {
        int id = PacketRegistry.getPacketId(packet.getClass());
        NetworkSide side = PacketRegistry.getPacketSide(packet.getClass());
        ByteBuf buffer = channel.alloc().buffer();
        packet.encodeInto(buffer);
        NettyPacket nettyPacket = new NettyPacket(id, buffer, side);
        channel.writeAndFlush(nettyPacket).addListener(new GenericFutureListener<Future<? super Void>>()
        {

            @Override
            public void operationComplete(Future<? super Void> future) throws Exception
            {
                if(!future.isSuccess())
                {
                    future.cause().printStackTrace();
                }
            }
        });
    }
}
