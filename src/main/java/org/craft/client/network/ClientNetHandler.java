package org.craft.client.network;

import static org.craft.network.ChannelHelper.*;
import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;

import org.craft.client.*;
import org.craft.client.gui.*;
import org.craft.entity.*;
import org.craft.network.*;
import org.craft.network.packets.*;
import org.craft.world.*;
import org.craft.world.loaders.*;
import org.craft.world.populators.*;

public class ClientNetHandler implements INetworkHandler
{
    private Channel  channel;
    private OurCraft game;

    public ClientNetHandler(OurCraft oc)
    {
        this.game = oc;
    }

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
                    setGuiStatus(e.toString());
                    Gui menu = game.getCurrentMenu();
                    if(menu instanceof GuiConnecting)
                    {
                        GuiConnecting connectingMenu = (GuiConnecting) menu;
                        connectingMenu.showGoBackButton();
                    }
                }
                finally
                {
                    workerGroup.shutdownGracefully();
                }

            }
        }.start();
    }

    @Override
    public void handlePacket(ChannelHandlerContext ctx, AbstractPacket packet)
    {
        if(packet instanceof S0ConnectionAccepted)
        {
            setGuiStatus("Connected... Downloading terrain");
            writeAndFlush(new C0PlayerInfos(game.getSession()), ctx);
            launchGame();
        }
        else if(packet instanceof S1ChatMessage)
        {
            S1ChatMessage chatMessage = (S1ChatMessage) packet;
            game.broadcastMessage(chatMessage.getMessage());
        }
        else if(packet instanceof S2ChunkData)
        {
            S2ChunkData chunkData = (S2ChunkData) packet;
            Chunk c = chunkData.getReadChunk();
            game.getClientWorld().addChunk(c);
        }
    }

    private void launchGame()
    {
        WorldGenerator generator = new WorldGenerator();
        generator.addPopulator(new RockPopulator());
        generator.addPopulator(new GrassPopulator());
        generator.addPopulator(new TreePopulator());
        generator.addPopulator(new FlowerPopulator());
        WorldLoader worldLoader;
        try
        {
            worldLoader = new FallbackWorldLoader();
            World clientWorld = new World("remote world", new RemoteChunkProvider(), generator, worldLoader);
            clientWorld.isRemote = true;
            EntityPlayer player = new EntityPlayer(clientWorld, game.getSession().getUUID());
            player.setLocation(0, 160 + 17, 0);
            clientWorld.spawn(player);
            game.getRenderEngine().setRenderViewEntity(player);
            game.setPlayerController(new RemotePlayerController(player, game));

            Entity testEntity = new Entity(clientWorld);
            testEntity.setLocation(player.posX + 10, player.posY + 20, player.posZ);
            clientWorld.spawn(testEntity);

            new ThreadGetChunksFromCamera(game).start();
            game.setWorld(clientWorld);
            game.setPlayer(player);
            game.openMenu(new GuiIngame(game));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnexionEstablished(ChannelHandlerContext ctx)
    {
        this.channel = ctx.channel();
    }

    public void setGuiStatus(String status)
    {
        Gui menu = game.getCurrentMenu();
        if(menu instanceof GuiConnecting)
        {
            GuiConnecting connectingMenu = (GuiConnecting) menu;
            connectingMenu.setStatus(status);
        }
    }

    public void send(AbstractPacket packet)
    {
        ChannelHelper.writeAndFlush(packet, channel);
    }
}
