package org.craft.client.network;

import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;

import org.craft.client.*;
import org.craft.client.gui.*;
import org.craft.network.*;
import org.craft.utils.*;

public class ClientNetHandler implements INetworkHandler
{
    public void connectTo(final String host, final int port)
    {
        new Thread("Client Multiplayer Thread")
        {
            public void run()
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
                            ch.pipeline().addLast(new PacketDecoder()).addLast(new PacketEncoder()).addLast(new NettyClientChannelHandler(ClientNetHandler.this));
                        }
                    });

                    // Start the client.
                    ChannelFuture f = b.connect(host, port).sync(); // (5)

                    // Wait until the connection is closed.
                    f.channel().closeFuture().sync();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    workerGroup.shutdownGracefully();
                }

            }
        }.start();
    }

    @Override
    public void handlePacket(AbstractPacket packet)
    {
        Log.message(packet.getClass().getName());
    }

    @Override
    public void onConnexionEstablished(ChannelHandlerContext ctx)
    {
        Gui menu = OurCraft.getOurCraft().getCurrentMenu();
        if(menu instanceof GuiConnecting)
        {
            GuiConnecting connectingMenu = (GuiConnecting) menu;
            connectingMenu.setStatus("Connected... Downloading terrain");
        }
    }
}
