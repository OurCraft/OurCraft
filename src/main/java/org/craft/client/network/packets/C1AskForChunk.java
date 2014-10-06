package org.craft.client.network.packets;

import io.netty.buffer.*;

import org.craft.network.*;

public class C1AskForChunk extends AbstractPacket
{

    private int chunkX;
    private int chunkY;
    private int chunkZ;

    public C1AskForChunk()
    {
        ;
    }

    public C1AskForChunk(int chunkX, int chunkY, int chunkZ)
    {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
    }

    @Override
    public void decodeFrom(ByteBuf buffer)
    {
        chunkX = buffer.readInt();
        chunkY = buffer.readInt();
        chunkZ = buffer.readInt();
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeInt(chunkX);
        buffer.writeInt(chunkY);
        buffer.writeInt(chunkZ);
    }

    public int getChunkX()
    {
        return chunkX;
    }

    public int getChunkY()
    {
        return chunkY;
    }

    public int getChunkZ()
    {
        return chunkZ;
    }

}
