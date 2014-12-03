package org.craft.spoonge.world;

import java.util.*;

import com.flowpowered.math.vector.*;
import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.spongepowered.api.block.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.world.biome.*;
import org.spongepowered.api.world.extent.*;

public class SpoongeExtent implements Extent
{

    private BlockLoc[]        blockPositions;
    private ArrayList<Entity> entities;

    public SpoongeExtent(BlockLoc... blockPositions)
    {
        this(blockPositions, Lists.<Entity> newArrayList());
    }

    public SpoongeExtent(BlockLoc[] blockPositions, ArrayList<Entity> entities)
    {
        this.blockPositions = blockPositions;
        this.entities = entities;
    }

    @Override
    public BlockLoc getBlock(Vector3d position)
    {
        Vector3i iPosition = position.toInt();
        return getBlock(iPosition.getX(), iPosition.getY(), iPosition.getZ());
    }

    @Override
    public BlockLoc getBlock(int x, int y, int z)
    {
        for(BlockLoc loc : blockPositions)
        {
            if(loc.getX() == x && loc.getY() == y && loc.getZ() == z)
            {
                return loc;
            }
        }
        return null;
    }

    @Override
    public Collection<Entity> getEntities()
    {
        return entities;
    }

    @Override
    public Optional<Entity> createEntity(EntityType type, Vector3d position)
    {
        throw new UnsupportedOperationException("Spawning entities isn't implemented yet");
    }

    @Override
    public Optional<Entity> createEntity(EntitySnapshot snapshot, Vector3d position)
    {
        throw new UnsupportedOperationException("Spawning entities isn't implemented yet");
    }

    @Override
    public Biome getBiome(Vector3d position)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
