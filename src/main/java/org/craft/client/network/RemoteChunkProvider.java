package org.craft.client.network;

import java.util.*;

import org.craft.client.*;
import org.craft.network.packets.*;
import org.craft.world.*;

import com.google.common.collect.Lists;

public class RemoteChunkProvider extends ChunkProvider
{

    private ChunkMap              chunkMap;
    private List<ChunkCoord> pending;
    private OurCraft              game;

    public RemoteChunkProvider()
    {
        this.game = OurCraft.getOurCraft();
        this.pending = Lists.newArrayList();
        chunkMap = new ChunkMap();
    }

    @Override
    public Chunk getOrCreate(World world, int chunkX, int chunkY, int chunkZ)
    {
        Chunk c = get(world, chunkX, chunkY, chunkZ);
        if(c == null)
            return create(world, chunkX, chunkY, chunkZ);
        return c;
    }

    @Override
    public Chunk get(World world, int chunkX, int chunkY, int chunkZ)
    {
        return chunkMap.getAt(chunkX, chunkY, chunkZ);
    }

    @Override
    public Chunk create(World world, int chunkX, int chunkY, int chunkZ)
    {
        ChunkCoord coords = ChunkCoord.get(chunkX, chunkY, chunkZ);
        if(!pending.contains(coords))
        {
            game.sendPacket(new C1AskForChunk(chunkX, chunkY, chunkZ));
        }
        return null;
    }

    @Override
    public boolean doesChunkExists(World world, int chunkX, int chunkY, int chunkZ)
    {
        ChunkCoord coords = ChunkCoord.get(chunkX, chunkY, chunkZ);
        if(pending.contains(coords))
        {
            return true;
        }
        else
            return chunkMap.contains(coords);
    }

    @Override
    public void addChunk(World w, Chunk c)
    {
        if(pending.contains(c.getCoords()))
            pending.remove(c.getCoords());
        if(!chunkMap.contains(c.getCoords()))
            chunkMap.add(c);
    }

    @Override
    public Iterator<Chunk> iterator()
    {
        return chunkMap.iterator();
    }

    /**
     * Removes a chunk from the pending list
     */
    public void removePending(ChunkCoord coords)
    {
        pending.remove(coords);
    }

}
