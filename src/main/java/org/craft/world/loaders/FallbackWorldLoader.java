package org.craft.world.loaders;

import java.io.*;

import org.craft.utils.*;
import org.craft.world.*;

public class FallbackWorldLoader extends WorldLoader
{

    public FallbackWorldLoader()
    {
        super(null, null);
    }

    public void loadWorldConstants(World world) throws IOException
    {
    }

    public void writeWorldConstants(File file, World world) throws IOException
    {
    }

    @Override
    public Chunk loadChunk(World world, int chunkX, int chunkY, int chunkZ) throws IOException
    {
        return null;
    }

    @Override
    public void writeChunk(ByteDataBuffer buffer, Chunk chunk, int chunkX, int chunkY, int chunkZ) throws IOException
    {
    }
}
