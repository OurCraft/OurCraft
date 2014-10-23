package org.craft.server.network.packets;

import io.netty.buffer.*;

import java.util.*;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
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
                        int nStates = buffer.readInt();
                        for(int i = 0; i < nStates; i++ )
                        {
                            BlockState state = BlockStates.getState(ByteBufUtils.readString(buffer));
                            if(state != null)
                            {
                                IBlockStateValue value = BlockStates.getValue(state, ByteBufUtils.readString(buffer));
                                chunk.setChunkBlockState(x, y, z, state, value);
                            }
                        }
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
                    BlockStatesObject o = chunk.getBlockStates(x, y, z);
                    if(o == null)
                    {
                        buffer.writeInt(0);
                    }
                    else
                    {
                        buffer.writeInt(o.size());
                        Iterator<BlockState> states = o.getMap().keySet().iterator();
                        while(states.hasNext())
                        {
                            BlockState state = states.next();
                            IBlockStateValue value = o.get(state);
                            ByteBufUtils.writeString(buffer, state.toString());
                            if(value == null)
                                ByteBufUtils.writeString(buffer, "null");
                            else
                                ByteBufUtils.writeString(buffer, value.toString());
                        }
                    }
                }
            }
        }
    }

    public Chunk getReadChunk()
    {
        return chunk;
    }

}
