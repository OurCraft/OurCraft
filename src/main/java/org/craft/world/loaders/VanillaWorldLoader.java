package org.craft.world.loaders;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.nbt.*;
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
            NBTCompoundTag worldInfos = NBTTag.readCompoundFromFile(loader.getResource(new ResourceLocation(worldFolder.getName(), "world.data")).asFile());
            world.setSeed(worldInfos.getLong("seed"));
        }
        catch(Exception e)
        {
            throw new IOException("Failed to load world constants", e);
        }
    }

    public void writeWorldConstants(File file, World world) throws IOException
    {
        try
        {
            NBTCompoundTag compoundTag = new NBTCompoundTag("worldData");
            compoundTag.putLong("seed", world.getSeed());
            compoundTag.putLong("timestamp", System.currentTimeMillis());
            compoundTag.putString("name", world.getName());
            NBTTag.writeCompoundToFile(file, compoundTag);
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }
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
                DataInputStream input = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(loader.getResource(res).getData()))));
                NBTCompoundTag chunkData = (NBTCompoundTag) NBTTag.readNamedTag(input);
                int readChunkX = chunkData.getInt("xCoord");
                int readChunkY = chunkData.getInt("yCoord");
                int readChunkZ = chunkData.getInt("zCoord");

                for(int x = 0; x < 16; x++ )
                {
                    for(int y = 0; y < 16; y++ )
                    {
                        for(int z = 0; z < 16; z++ )
                        {
                            NBTCompoundTag blockData = chunkData.getCompound(x + "." + y + "." + z);
                            String blockId = blockData.getString("id");
                            Block b = Blocks.get(blockId);
                            if(b != null)
                            {
                                chunk.setChunkBlock(x, y, z, b);
                            }
                            else
                                Log.message("Unknown block at " + x + "," + y + "," + z + " = " + blockId);
                            NBTCompoundTag blockStates = blockData.getCompound("blockStates");
                            for(NBTTag tag : blockStates.getAllTags())
                            {
                                if(tag instanceof NBTStringTag)
                                {
                                    NBTStringTag blockStateData = (NBTStringTag) tag;
                                    BlockState state = BlockStates.getState(blockStateData.getName());
                                    IBlockStateValue value = BlockStates.getValue(state, blockStateData.getData());
                                    chunk.setBlockState(x, y, z, state, value);
                                }
                                else
                                    throw new IllegalArgumentException("Block state tag is not a NBTStringTag, it's a instance of " + tag.getClass().getCanonicalName());
                            }
                        }
                    }
                }
                input.close();
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
    public void writeChunk(File file, Chunk chunk, int chunkX, int chunkY, int chunkZ) throws IOException
    {
        NBTCompoundTag tag = new NBTCompoundTag("chunkData");
        tag.putInt("xCoord", chunkX);
        tag.putInt("yCoord", chunkY);
        tag.putInt("zCoord", chunkZ);
        for(int x = 0; x < 16; x++ )
        {
            for(int y = 0; y < 16; y++ )
            {
                for(int z = 0; z < 16; z++ )
                {
                    NBTCompoundTag blockData = new NBTCompoundTag();
                    blockData.putString("id", chunk.getChunkBlock(x, y, z).getId());
                    BlockStatesObject o = chunk.getBlockStates(x, y, z);
                    NBTCompoundTag blockStates = new NBTCompoundTag();
                    if(o == null)
                    {
                    }
                    else
                    {
                        Iterator<BlockState> states = o.getMap().keySet().iterator();
                        while(states.hasNext())
                        {
                            BlockState state = states.next();
                            IBlockStateValue value = o.get(state);
                            blockStates.putString(state.toString(), value == null ? "null" : value.toString());
                        }
                    }
                    blockData.putCompound("blockStates", blockStates);
                    tag.putCompound(x + "." + y + "." + z, blockData);
                }
            }
        }
        NBTTag.writeCompoundToFile(file, tag);
    }

    public NBTCompoundTag loadWorldInfos(File worldDataFile) throws IOException
    {
        return NBTTag.readCompoundFromFile(worldDataFile);
    }
}
