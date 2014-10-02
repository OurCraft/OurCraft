package org.craft.network;

import io.netty.channel.*;

public interface INetworkHandler
{

    void handlePacket(AbstractPacket packet);

    void onConnexionEstablished(ChannelHandlerContext ctx);
}
