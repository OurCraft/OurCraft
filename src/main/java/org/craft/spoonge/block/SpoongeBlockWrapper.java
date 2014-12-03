package org.craft.spoonge.block;

import java.util.*;

import com.flowpowered.math.vector.*;
import com.google.common.base.Optional;

import org.craft.blocks.*;
import org.craft.spoonge.modifiers.*;
import org.craft.world.World;
import org.spongepowered.api.block.*;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.util.*;
import org.spongepowered.api.world.*;
import org.spongepowered.api.world.extent.*;

public class SpoongeBlockWrapper implements BlockLoc
{

    private Block    implBlock;
    private int      x;
    private int      y;
    private int      z;
    private World    world;
    private Vector3i pos;

    public SpoongeBlockWrapper(Block block, int x, int y, int z, World world)
    {
        this.implBlock = block;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pos = new Vector3i(x, y, z);
        this.world = world;
    }

    @Override
    public BlockSnapshot getSnapshot()
    {
        return new SpoongeBlockSnapshot(getState());
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
        world.setBlock(x, y, z, (Block) type);
    }

    @Override
    public void replaceWith(BlockSnapshot snapshot)
    {
        world.setBlock(x, y, z, (Block) snapshot.getState().getType());
    }

    @Override
    public void interact()
    {
        implBlock.onBlockClicked(world, x, y, z, null);
    }

    @Override
    public void interactWith(ItemStack itemStack)
    {
        implBlock.onBlockClicked(world, x, y, z, null);
        // TODO: ItemStack
    }

    @Override
    public void replaceWith(BlockState state)
    {
        world.setBlock(x, y, z, (Block) state.getType());
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
        SpoongeBlockState blockState = new SpoongeBlockState();
        blockState.setType(getType());
        blockState.setMap(world.getBlockStates(x, y, z));
        return blockState;
    }

    @Override
    public BlockType getType()
    {
        return (BlockType) implBlock;
    }

    @Override
    public int getDigTime()
    {
        return 0;
    }

    @Override
    public int getDigTimeWith(ItemStack itemStack)
    {
        return 0;
    }

    @Override
    public <T> Optional<T> getData(Class<T> dataClass)
    {
        return Optional.absent();
    }

}
