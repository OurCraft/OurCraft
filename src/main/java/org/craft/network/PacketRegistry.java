package org.craft.network;

import java.util.*;

import org.craft.network.packets.*;
import org.craft.server.network.packets.*;
import org.craft.utils.*;

public class PacketRegistry
{

    private static HashMap<NetworkSide, HashMap<Integer, Class<? extends AbstractPacket>>> packets;

    public static void init()
    {
        packets = new HashMap<NetworkSide, HashMap<Integer, Class<? extends AbstractPacket>>>();
        packets.put(NetworkSide.CLIENT, new HashMap<Integer, Class<? extends AbstractPacket>>());
        packets.put(NetworkSide.COMMON, new HashMap<Integer, Class<? extends AbstractPacket>>());
        packets.put(NetworkSide.SERVER, new HashMap<Integer, Class<? extends AbstractPacket>>());
        registerPacket(NetworkSide.COMMON, 0x0, TestPacket.class);
        registerPacket(NetworkSide.SERVER, 0x0, S0ConnectionAccepted.class);
    }

    public static void registerPacket(NetworkSide senderSide, int id, Class<? extends AbstractPacket> packetClass)
    {
        packets.get(senderSide).put(id, packetClass);
    }

    public static AbstractPacket create(NetworkSide side, int id)
    {
        Class<? extends AbstractPacket> packet = packets.get(side).get(id);
        if(packet == null)
        {
            Log.fatal("Unknown packet id: " + id);
        }
        try
        {
            return packet.newInstance();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
