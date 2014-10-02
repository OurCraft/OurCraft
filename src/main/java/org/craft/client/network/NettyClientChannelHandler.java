package org.craft.client.network;

import org.craft.network.*;

public class NettyClientChannelHandler extends ChannelHandler
{

    public NettyClientChannelHandler(INetworkHandler netHandler)
    {
        super(netHandler);
    }

}
