package org.craft.server.network;

import io.netty.channel.*;

import org.craft.network.ChannelHandler;
import org.craft.server.network.packets.*;
import org.craft.utils.*;

public class NettyServerChannelHandler extends ChannelHandler
{

    public NettyServerChannelHandler()
    {
        super(new ServerNetHandler());
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx)
    {
        Log.message("new client");
        write(new S0ConnectionAccepted(), ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
