package org.craft.server.network;

import io.netty.channel.*;

import org.craft.client.*;
import org.craft.client.network.packets.*;
import org.craft.client.render.fonts.*;
import org.craft.network.*;
import org.craft.server.*;
import org.craft.utils.*;

public class ServerNetHandler implements INetworkHandler
{

    @Override
    public void handlePacket(ChannelHandlerContext ctx, AbstractPacket packet)
    {
        if(packet instanceof C0PlayerInfos)
        {
            C0PlayerInfos playerInfos = (C0PlayerInfos) packet;
            SessionManager.getInstance().registerSession(playerInfos.getSession());
            OurCraftServer.getServer().getNettyWrapper().registerChannel(playerInfos.getSession().getId(), ctx.channel());
            OurCraftServer.getServer().broadcastMessage(TextFormatting.generateFromColor(200, 200, 50) + I18n.format("players.joined", playerInfos.getSession().getDisplayName()));
        }
    }

    @Override
    public void onConnexionEstablished(ChannelHandlerContext ctx)
    {
        ;
    }

}
