package org.craft.spoonge.modifiers;

import java.util.*;

import com.flowpowered.math.vector.*;
import com.google.common.base.Optional;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.effect.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.item.*;
import org.spongepowered.api.world.*;
import org.spongepowered.api.world.biome.*;
import org.spongepowered.api.world.weather.*;

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
    public BlockLoc getBlock(Vector3d position)
    {
        return (BlockLoc) getBlockAt((int) Math.floor(position.getX()), (int) Math.floor(position.getY()), (int) Math.floor(position.getZ()));
    }

    @Override
    public BlockLoc getBlock(int x, int y, int z)
    {
        return (BlockLoc) getBlockAt(x, y, z);
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

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed, int radius)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed, ItemType itemType)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed, int radius, ItemType itemType)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Sound sound, Vector3d position, double volume)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Sound sound, Vector3d position, double volume, double pitch)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Sound sound, Vector3d position, double volume, double pitch, double minVolume)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Weather getWeather()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getRemainingDuration()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getRunningDuration()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void forecast(Weather weather)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void forecast(Weather weather, long duration)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public WorldBorder getWorldBorder()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<String> getGameRule(String gameRule)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGameRule(String gameRule, String value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, String> getGameRules()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
