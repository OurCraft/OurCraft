package org.craft.world.loaders;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.world.*;

public class VanillaWorldLoader extends WorldLoader
{

    public VanillaWorldLoader(ResourceLocation worldFolder, ResourceLoader loader)
    {
        super(worldFolder, loader);
    }

    public void loadWorldConstants(World world) throws IOException
    {
        try
        {
            ByteDataBuffer buffer = new ByteDataBuffer(loader.getResource(new ResourceLocation(worldFolder.getName(), "world.data")).getData());
            world.setSeed(buffer.readLong());
            buffer.close();
        }
        catch(Exception e)
        {
            throw new IOException("Failed to load world constants", e);
        }
    }

    public void writeWorldConstants(ByteDataBuffer buffer, World world) throws IOException
    {
        buffer.writeLong(world.getSeed());
        buffer.writeLong(System.currentTimeMillis());
        buffer.writeString(world.getName());
    }

    @Override
    public Chunk loadChunk(World world, int chunkX, int chunkY, int chunkZ) throws IOException
    {
        ResourceLocation res = new ResourceLocation(worldFolder, "chunkData/chunk" + chunkX + "." + chunkY + "." + chunkZ + ".data");
        if(loader.doesResourceExists(res))
        {
            Chunk chunk = new Chunk(world, ChunkCoord.get(chunkX, chunkY, chunkZ));
            try
            {
                ByteDataBuffer buffer = new ByteDataBuffer(loader.getResource(res).getData());
                int readChunkX = buffer.readInt();
                int readChunkY = buffer.readInt();
                int readChunkZ = buffer.readInt();

                for(int x = 0; x < 16; x++ )
                {
                    for(int y = 0; y < 16; y++ )
                    {
                        for(int z = 0; z < 16; z++ )
                        {
                            String blockId = buffer.readString();
                            Block b = Blocks.get(blockId);
                            if(b != null)
                            {
                                chunk.setChunkBlock(x, y, z, b);
                            }
                            else
                                Log.message("Unknown block at " + x + "," + y + "," + z + " = " + blockId);
                            int nBlockStates = buffer.readInt();
                            for(int i = 0; i < nBlockStates; i++ )
                            {
                                BlockState state = BlockStates.getState(buffer.readString());
                                IBlockStateValue value = BlockStates.getValue(state, buffer.readString());
                                chunk.setChunkBlockState(x, y, z, state, value);
                            }
                        }
                    }
                }
                buffer.close();
                assert readChunkX == chunkX : "Read chunkX is not equal to given chunkX";
                assert readChunkY == chunkY : "Read chunkY is not equal to given chunkY";
                assert readChunkZ == chunkZ : "Read chunkZ is not equal to given chunkZ";
            }
            catch(Exception e)
            {
                throw new IOException("Failed to load chunk (" + chunkX + "," + chunkY + "," + chunkZ + ")", e);
            }
            return chunk;
        }
        return null;
    }

    @Override
    public void writeChunk(ByteDataBuffer buffer, Chunk chunk, int chunkX, int chunkY, int chunkZ) throws IOException
    {
        buffer.writeInt(chunkX);
        buffer.writeInt(chunkY);
        buffer.writeInt(chunkZ);
        for(int x = 0; x < 16; x++ )
        {
            for(int y = 0; y < 16; y++ )
            {
                for(int z = 0; z < 16; z++ )
                {
                    buffer.writeString(chunk.getChunkBlock(x, y, z).getId());
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
                            buffer.writeString(state.toString());
                            buffer.writeString(value.toString());
                        }
                    }
                }
            }
        }
    }

    public HashMap<String, String> loadWorldInfos(File worldDataFile)
    {
        HashMap<String, String> map = Maps.newHashMap();
        try
        {
            ByteDataBuffer buffer = new ByteDataBuffer(new BufferedInputStream(new FileInputStream(worldDataFile)));
            map.put("seed", "" + buffer.readLong());
            if(buffer.readableBytes() >= 8)
            {
                map.put("timestamp", buffer.readLong() + "");
                map.put("name", buffer.readString());
            }
            buffer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return map;
    }
}
