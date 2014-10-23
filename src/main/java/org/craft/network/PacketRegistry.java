package org.craft.network;

import java.util.*;

import org.craft.network.packets.*;
import org.craft.utils.*;

public class PacketRegistry
{

    private static HashMap<NetworkSide, HashMap<Integer, Class<? extends AbstractPacket>>> packets;

    private static HashMap<Class<? extends AbstractPacket>, NetworkSide>                   sides;
    private static HashMap<Class<? extends AbstractPacket>, Integer>                       ids;

    public static void init()
    {
        sides = new HashMap<Class<? extends AbstractPacket>, NetworkSide>();
        ids = new HashMap<Class<? extends AbstractPacket>, Integer>();
        packets = new HashMap<NetworkSide, HashMap<Integer, Class<? extends AbstractPacket>>>();
        packets.put(NetworkSide.CLIENT, new HashMap<Integer, Class<? extends AbstractPacket>>());
        packets.put(NetworkSide.COMMON, new HashMap<Integer, Class<? extends AbstractPacket>>());
        packets.put(NetworkSide.SERVER, new HashMap<Integer, Class<? extends AbstractPacket>>());

        registerPacket(NetworkSide.CLIENT, 0x0, C0PlayerInfos.class);
        registerPacket(NetworkSide.CLIENT, 0x1, C1AskForChunk.class);

        registerPacket(NetworkSide.SERVER, 0x0, S0ConnectionAccepted.class);
        registerPacket(NetworkSide.SERVER, 0x1, S1ChatMessage.class);
        registerPacket(NetworkSide.SERVER, 0x2, S2ChunkData.class);
    }

    public static int getPacketId(Class<? extends AbstractPacket> packet)
    {
        return ids.get(packet);
    }

    public static NetworkSide getPacketSide(Class<? extends AbstractPacket> packet)
    {
        return sides.get(packet);
    }

    public static void registerPacket(NetworkSide senderSide, int id, Class<? extends AbstractPacket> packetClass)
    {
        packets.get(senderSide).put(id, packetClass);

        sides.put(packetClass, senderSide);
        ids.put(packetClass, id);
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
