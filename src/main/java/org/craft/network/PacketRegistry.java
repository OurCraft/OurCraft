package org.craft.network;

import java.util.*;

import org.craft.network.packets.*;
import org.craft.utils.*;

public class PacketRegistry
{

    private static HashMap<Integer, Class<? extends AbstractPacket>> packets;

    public static void init()
    {
        packets = new HashMap<Integer, Class<? extends AbstractPacket>>();
        registerPacket(0x0, TestPacket.class);
    }

    public static void registerPacket(int id, Class<? extends AbstractPacket> packetClass)
    {
        packets.put(id, packetClass);
    }

    public static AbstractPacket create(int id)
    {
        Class<? extends AbstractPacket> packet = packets.get(id);
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
