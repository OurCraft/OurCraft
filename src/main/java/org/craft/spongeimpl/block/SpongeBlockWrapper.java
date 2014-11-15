package org.craft.spongeimpl.block;

import java.util.*;

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

public class SpongeBlockWrapper implements org.spongepowered.api.block.Block
{

    private Block implBlock;
    private int   x;
    private int   y;
    private int   z;
    private World world;
    private Vec3i pos;

    public SpongeBlockWrapper(Block block, int x, int y, int z, World world)
    {
        this.implBlock = block;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pos = new Vec3i(x, y, z);
        this.world = world;
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
        return pos;
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
    public void replaceWith(BlockState state)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public Collection<Direction> getPoweredFaces()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Direction> getIndirectlyPoweredFaces()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockState getState()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
