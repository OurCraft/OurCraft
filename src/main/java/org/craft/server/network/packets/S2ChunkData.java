package org.craft.server.network.packets;

import io.netty.buffer.*;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.network.*;
import org.craft.utils.*;
import org.craft.world.*;

public class S2ChunkData extends AbstractPacket
{
    private Chunk chunk;

    public S2ChunkData()
    {
    }

    public S2ChunkData(Chunk c)
    {
        this.chunk = c;
    }

    @Override
    public void decodeFrom(ByteBuf buffer)
    {
        try
        {
            int readChunkX = buffer.readInt();
            int readChunkY = buffer.readInt();
            int readChunkZ = buffer.readInt();

            chunk = new Chunk(OurCraft.getOurCraft().getClientWorld(), ChunkCoord.get(readChunkX, readChunkY, readChunkZ));
            for(int x = 0; x < 16; x++ )
            {
                for(int y = 0; y < 16; y++ )
                {
                    for(int z = 0; z < 16; z++ )
                    {
                        String blockId = ByteBufUtils.readString(buffer);
                        Block b = Blocks.get(blockId);
                        if(b != null)
                        {
                            chunk.setChunkBlock(x, y, z, b);
                        }
                        else
                            Log.message("Unknown block at " + x + "," + y + "," + z + " = " + blockId);
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeInt(chunk.getCoords().x);
        buffer.writeInt(chunk.getCoords().y);
        buffer.writeInt(chunk.getCoords().z);
        for(int x = 0; x < 16; x++ )
        {
            for(int y = 0; y < 16; y++ )
            {
                for(int z = 0; z < 16; z++ )
                {
                    ByteBufUtils.writeString(buffer, chunk.getChunkBlock(x, y, z).getId());
                }
            }
        }
    }

    public Chunk getReadChunk()
    {
        return chunk;
    }

}
