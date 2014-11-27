package org.craft.spongeimpl.modifiers;

import java.util.*;

import com.google.common.base.Optional;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.math.*;
import org.spongepowered.api.world.*;
import org.spongepowered.api.world.biome.*;

@BytecodeModifier("org.craft.world.World")
public class SpoongeWorld implements World
{

    //===========================================================
    //               START OF SHADOW METHODS
    //===========================================================
    @Shadow
    public org.craft.blocks.Block getBlockAt(int x, int y, int z)
    {
        return null;
    }

    @Shadow
    @Override
    public String getName()
    {
        return null;
    }

    //===========================================================
    //               END OF SHADOW METHODS
    //===========================================================

    @Override
    public Block getBlock(Vector3d position)
    {
        return (Block) getBlockAt((int) Math.floor(position.getX()), (int) Math.floor(position.getY()), (int) Math.floor(position.getZ()));
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        return (Block) getBlockAt(x, y, z);
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

    @Override
    public Optional<Entity> getEntityFromUUID(UUID uuid)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
