package org.craft.server.network;

import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;
import io.netty.util.concurrent.*;

import java.util.*;

import org.craft.modding.events.*;
import org.craft.network.*;
import org.craft.spongeimpl.events.state.*;
import org.spongepowered.api.*;

/**
 * Discards any incoming data.
 */
public class NettyServerWrapper implements Runnable
{

    private int                      port;
    private EventBus                 eventBus;
    private Game                     game;
    private ArrayList<Channel>       channels;
    private HashMap<String, Channel> channelsMap;
    private Channel                  serverChannel;

    public NettyServerWrapper(Game gameInstance, EventBus eventBus, int port)
    {
        channels = new ArrayList<Channel>();
        channelsMap = new HashMap<String, Channel>();

        this.game = gameInstance;
        this.port = port;
        this.eventBus = eventBus;
    }

    public void run()
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new PacketDecoder()).addLast(new PacketEncoder()).addLast(new NettyServerChannelHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            eventBus.call(new SpongeServerStartingEvent(game));
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).addListener(new GenericFutureListener<Future<? super Void>>()
            {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception
                {
                    if(future.isSuccess())
                        eventBus.call(new SpongeServerStartedEvent(game));
                }
            }).sync();

            eventBus.call(new SpongeServerStoppingEvent(game));
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to
            // gracefully
            // shut down your server.
            serverChannel = f.channel();
            serverChannel.closeFuture().sync();
            eventBus.call(new SpongeServerStoppedEvent(game));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void registerChannel(String id, Channel channel)
    {
        channelsMap.put(id, channel);
    }

    public void sendPacketToAll(AbstractPacket packet)
    {
        for(Channel channel : channelsMap.values())
        {
            ChannelHelper.writeAndFlush(packet, channel);
        }
    }

    public void stop()
    {
        serverChannel.close();
    }
}
