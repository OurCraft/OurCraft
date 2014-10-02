package org.craft.server.network;

import io.netty.channel.*;

import org.craft.network.*;
import org.craft.utils.*;

public class ServerNetHandler implements INetworkHandler
{

    @Override
    public void handlePacket(AbstractPacket packet)
    {
        Log.message(packet.getClass().getCanonicalName());
    }

    @Override
    public void onConnexionEstablished(ChannelHandlerContext ctx)
    {
        ;
    }

}
