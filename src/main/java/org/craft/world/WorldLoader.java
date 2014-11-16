package org.craft.world;

import java.io.*;

import com.mojang.nbt.*;

import org.craft.resources.*;

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

    public abstract void writeWorldConstants(File file, World world) throws IOException;

    public abstract Chunk loadChunk(World world, int chunkX, int chunkY, int chunkZ) throws IOException;

    public abstract void writeChunk(File file, Chunk chunk, int chunkX, int chunkY, int chunkZ) throws IOException;

    public NBTCompoundTag loadWorldInfos(File worldDataFile) throws IOException
    {
        return null;
    }
}
