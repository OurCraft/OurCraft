package org.craft.network;

import io.netty.channel.*;

public interface INetworkHandler
{

    void handlePacket(ChannelHandlerContext ctx, AbstractPacket packet);

    void onConnexionEstablished(ChannelHandlerContext ctx);
}
