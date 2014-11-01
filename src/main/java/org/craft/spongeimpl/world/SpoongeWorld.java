package org.craft.spongeimpl.world;

import java.util.*;

import com.google.common.base.Optional;

import org.spongepowered.api.block.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.math.*;
import org.spongepowered.api.world.*;
import org.spongepowered.api.world.biome.*;

public class SpoongeWorld implements World
{

    private org.craft.world.World realWorld;

    public SpoongeWorld(org.craft.world.World ocWorld)
    {
        this.realWorld = ocWorld;
    }

    @Override
    public Block getBlock(Vector3d position)
    {
        return (Block) realWorld.getBlockAt((int) Math.floor(position.getX()), (int) Math.floor(position.getY()), (int) Math.floor(position.getZ()));
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getEntities()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Entity> createEntity(EntityType type, Vector3d position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Entity> createEntity(EntitySnapshot snapshot, Vector3d position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Biome getBiome(Vector3d position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UUID getUniqueID()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Chunk> getChunk(Vector2i position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Chunk> loadChunk(Vector2i position, boolean shouldGenerate)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Chunk loadChunk(Vector2i position)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
