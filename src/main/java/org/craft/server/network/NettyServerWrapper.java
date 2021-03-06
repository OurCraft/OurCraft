package org.craft.server.network;

import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;
import io.netty.util.concurrent.*;

import java.util.*;

import org.craft.entity.*;
import org.craft.modding.events.*;
import org.craft.modding.events.state.*;
import org.craft.network.*;
import org.craft.server.*;

/**
 */
public class NettyServerWrapper implements Runnable
{

    private int                              port;
    private EventBus                         eventBus;
    private OurCraftServer                   game;
    private HashMap<String, Channel>         channelsMap;
    private Channel                          serverChannel;
    private HashMap<Channel, EntityPlayerMP> channels2players;
    private HashMap<EntityPlayerMP, Channel> players2channels;

    public NettyServerWrapper(OurCraftServer gameInstance, EventBus eventBus, int port)
    {
        channelsMap = new HashMap<String, Channel>();
        channels2players = new HashMap<Channel, EntityPlayerMP>();
        players2channels = new HashMap<EntityPlayerMP, Channel>();

        this.game = gameInstance;
        this.port = port;
        this.eventBus = eventBus;
    }

    @Override
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

            eventBus.fireEvent(new ModServerStartingEvent(game), null, null);
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).addListener(new GenericFutureListener<Future<? super Void>>()
            {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception
                {
                    if(future.isSuccess())
                        eventBus.fireEvent(new ModServerStartedEvent(game));
                }
            }).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to
            // gracefully
            // shut down your server.
            serverChannel = f.channel();
            serverChannel.closeFuture().sync();
            eventBus.fireEvent(new ModServerStoppingEvent(game));
            eventBus.fireEvent(new ModServerStoppedEvent(game));
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

    public void registerChannel(String id, Channel channel, EntityPlayerMP player)
    {
        channelsMap.put(id, channel);
        channels2players.put(channel, player);
        players2channels.put(player, channel);
    }

    public void sendPacketToAll(AbstractPacket packet)
    {
        for(Channel channel : channelsMap.values())
        {
            ChannelHelper.writeAndFlush(packet, channel);
        }
    }

    public void sendPacketToPlayer(AbstractPacket packet, EntityPlayerMP player)
    {
        Channel channel = players2channels.get(player);
        if(channel != null)
            ChannelHelper.writeAndFlush(packet, channel);
    }

    public void stop()
    {
        serverChannel.close();
    }
}
