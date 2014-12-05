package org.craft.spoonge.block;

import java.util.*;

import com.flowpowered.math.vector.*;
import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.spoonge.modifiers.*;
import org.craft.spoonge.util.*;
import org.craft.spoonge.world.*;
import org.craft.utils.*;
import org.craft.world.World;
import org.spongepowered.api.block.*;
import org.spongepowered.api.block.BlockState;
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
    private Location location;
    private Extent   extent;

    public SpoongeBlockWrapper(Block block, int x, int y, int z, World world)
    {
        this.implBlock = block;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pos = new Vector3i(x, y, z);
        this.world = world;
        extent = new SpoongeExtent(this);
        this.location = new Location(extent, new Vector3d(x, y, z));
    }

    @Override
    public BlockSnapshot getSnapshot()
    {
        return new SpoongeBlockSnapshot(getState());
    }

    @Override
    public boolean isPowered()
    {
        return world.getDirectElectricPowerAt(x, y, z) != 0;
    }

    @Override
    public boolean isIndirectlyPowered()
    {
        return isPowered();
    }

    @Override
    public boolean isFacePowered(Direction direction)
    {
        EnumSide side = DirectionUtils.fromDirection(direction);
        int x1 = side.getTranslationX() + x;
        int y1 = side.getTranslationY() + y;
        int z1 = side.getTranslationZ() + z;
        IBlockStateValue value = world.getBlockState(x1, y1, z1, BlockStates.powered);
        if(value != null)
        {
            return value.toString().equals("true");
        }
        return false;
    }

    @Override
    public boolean isFaceIndirectlyPowered(Direction direction)
    {
        return isFacePowered(direction);
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
        return 0;
    }

    @Override
    public byte getLuminanceFromSky()
    {
        return 0;
    }

    @Override
    public byte getLuminanceFromGround()
    {
        return 0;
    }

    @Override
    public Extent getExtent()
    {
        return extent;
    }

    @Override
    public Vector3i getPosition()
    {
        return pos;
    }

    @Override
    public Location getLocation()
    {
        return location;
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
        interactWith(null);
    }

    @Override
    public void interactWith(ItemStack itemStack)
    {
        implBlock.onBlockClicked(world, x, y, z, null, (org.craft.inventory.Stack) itemStack);
    }

    @Override
    public void replaceWith(BlockState state)
    {
        world.setBlock(x, y, z, (Block) state.getType());
    }

    @Override
    public Collection<Direction> getPoweredFaces()
    {
        List<Direction> directions = Lists.newArrayList();
        for(Direction direct : Direction.values())
        {
            if(isFacePowered(direct))
                directions.add(direct);
        }
        return directions;
    }

    @Override
    public Collection<Direction> getIndirectlyPoweredFaces()
    {
        return getPoweredFaces();
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
