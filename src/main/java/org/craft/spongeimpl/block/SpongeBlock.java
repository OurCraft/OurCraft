package org.craft.spongeimpl.block;

<<<<<<< HEAD
import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;
=======
import java.util.Collection;
>>>>>>> origin/master

import org.craft.blocks.*;
import org.craft.blocks.Block;
import org.craft.spongeimpl.math.*;
import org.craft.world.World;
import org.spongepowered.api.block.*;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.math.*;
import org.spongepowered.api.util.*;
import org.spongepowered.api.world.*;
import org.spongepowered.api.world.extent.*;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

public class SpongeBlock implements org.spongepowered.api.block.Block
{

    private Block implBlock;
    private int   x;
    private int   y;
    private int   z;
    private World world;

    public SpongeBlock(Block block, int x, int y, int z, World world)
    {
        this.implBlock = block;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @Override
    public BlockType getType()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte getDataValue()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public BlockSnapshot getSnapshot()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPowered()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isIndirectlyPowered()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isFacePowered(Direction direction)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isFaceIndirectlyPowered(Direction direction)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean dig()
    {
        world.setBlock(x, y, z, Blocks.air);
        return true;
    }

    @Override
    public boolean digWith(ItemStack itemStack)
    {
        return dig();
    }

    @Override
    public byte getLuminance()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getLuminanceFromSky()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte getLuminanceFromGround()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Extent getExtent()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector3i getPosition()
    {
        return new Vec3i(x, y, z);
    }

    @Override
    public Location getLocation()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    @Override
    public int getZ()
    {
        return z;
    }

    @Override
    public void replaceWith(BlockType type)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void replaceWith(BlockSnapshot snapshot)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void interact()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void interactWith(ItemStack itemStack)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public ImmutableMap<BlockProperty<?>, ? extends Comparable<?>> getProperties()
<<<<<<< HEAD
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> getPropertyNames()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<BlockProperty<?>> getPropertyByName(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<? extends Comparable<?>> getPropertyValue(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockState withProperty(BlockProperty<?> property, Comparable<?> value)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockState cycleProperty(BlockProperty<?> property)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void replaceWith(BlockState state)
=======
>>>>>>> origin/master
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> getPropertyNames()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<BlockProperty<?>> getPropertyByName(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<? extends Comparable<?>> getPropertyValue(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockState withProperty(BlockProperty<?> property, Comparable<?> value)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockState cycleProperty(BlockProperty<?> property)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void replaceWith(BlockState state)
    {
        // TODO Auto-generated method stub
        
    }

}
