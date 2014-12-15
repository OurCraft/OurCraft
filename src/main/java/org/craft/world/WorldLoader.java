package org.craft.world;

import java.io.*;
import java.util.*;

import com.mojang.nbt.*;

import org.craft.entity.*;
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

    public abstract void writeEntities(List<Entity> entitiesList) throws IOException;

    public abstract NBTListTag<NBTCompoundTag> loadPlayersInfos(World w) throws IOException;
}
