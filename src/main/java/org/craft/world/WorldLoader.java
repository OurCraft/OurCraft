package org.craft.world;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.resources.*;
import org.craft.utils.*;

public abstract class WorldLoader
{

    protected ResourceLocation worldFolder;
    protected ResourceLoader   loader;

    public WorldLoader(ResourceLocation worldFolder, ResourceLoader loader)
    {
        this.worldFolder = worldFolder;
        this.loader = loader;
    }

    public ResourceLocation getFolder()
    {
        return worldFolder;
    }

    public abstract void loadWorldConstants(World world) throws IOException;

    public abstract void writeWorldConstants(ByteDataBuffer buffer, World world) throws IOException;

    public abstract Chunk loadChunk(World world, int chunkX, int chunkY, int chunkZ) throws IOException;

    public abstract void writeChunk(ByteDataBuffer buffer, Chunk chunk, int chunkX, int chunkY, int chunkZ) throws IOException;

    public HashMap<String, String> loadWorldInfos(File worldDataFile)
    {
        return Maps.newHashMap();
    }
}
