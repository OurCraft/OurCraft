package org.craft.client.network;

import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;

import org.craft.network.*;

public class ClientNetHandler
{
    public void connectTo(String host, int port) throws Exception
    {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel ch) throws Exception
                {
                    ch.pipeline().addLast(new PacketDecoder()).addLast(new PacketEncoder()).addLast(new NettyClientChannelHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        }
        finally
        {
            workerGroup.shutdownGracefully();
        }
    }
}
