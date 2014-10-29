package org.craft.server.network;

import io.netty.channel.*;

import org.craft.client.*;
import org.craft.client.render.fonts.*;
import org.craft.entity.*;
import org.craft.network.*;
import org.craft.network.packets.*;
import org.craft.server.*;
import org.craft.utils.*;
import org.craft.world.*;

public class ServerNetHandler implements INetworkHandler
{

    @Override
    public void handlePacket(ChannelHandlerContext ctx, AbstractPacket packet)
    {
        if(packet instanceof C0PlayerInfos)
        {
            C0PlayerInfos playerInfos = (C0PlayerInfos) packet;
            SessionManager.getInstance().registerSession(playerInfos.getSession());
            EntityPlayerMP playerEntity = new EntityPlayerMP(OurCraftServer.getServer().getServerWorld(), playerInfos.getSession().getUUID());
            OurCraftServer.getServer().getServerWorld().spawn(playerEntity);
            OurCraftServer.getServer().getNettyWrapper().registerChannel(playerInfos.getSession().getId(), ctx.channel(), playerEntity);
            OurCraftServer.getServer().broadcastMessage(TextFormatting.generateFromColor(200, 200, 50) + I18n.format("players.joined", playerInfos.getSession().getDisplayName()));
        }
        else if(packet instanceof C1AskForChunk)
        {
            C1AskForChunk chunkAsked = (C1AskForChunk) packet;
            World w = OurCraftServer.getServer().getServerWorld();
            Chunk c = w.getChunkProvider().getOrCreate(w, chunkAsked.getChunkX(), chunkAsked.getChunkY(), chunkAsked.getChunkZ());
            ChannelHelper.writeAndFlush(new S2ChunkData(c), ctx);
        }
    }

    @Override
    public void onConnexionEstablished(ChannelHandlerContext ctx)
    {
        ;
    }

}
