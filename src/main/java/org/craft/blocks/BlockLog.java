package org.craft.blocks;

import org.craft.blocks.states.*;
import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockLog extends Block
{

    private TextureIcon sideIcon;
    private TextureIcon topAndBottomIcon;
    private TextureIcon lyingSideIcon;

    public BlockLog(String id)
    {
        super(id);
    }

    public TextureIcon getBlockIcon(World world, int x, int y, int z, EnumSide side)
    {
        IBlockStateValue value = world.getBlockState(x, y, z, BlockStates.ORIENTATION);
        if(value == null || !(value instanceof EnumLogBlockStates))
            value = EnumLogBlockStates.UP;
        if(value == EnumLogBlockStates.UP)
        {
            if(side == EnumSide.BOTTOM || side == EnumSide.TOP)
                return topAndBottomIcon;
            return sideIcon;
        }
        else
        {
            if(value == EnumLogBlockStates.LYING_NS)
            {
                if(side == EnumSide.NORTH || side == EnumSide.SOUTH)
                    return topAndBottomIcon;
                if(side == EnumSide.EAST || side == EnumSide.WEST)
                    return lyingSideIcon;
                return sideIcon;
            }
            else if(value == EnumLogBlockStates.LYING_WE)
            {
                if(side == EnumSide.WEST || side == EnumSide.EAST)
                    return topAndBottomIcon;
                return lyingSideIcon;
            }
            return sideIcon;
        }
    }

    public void registerIcons(IconGenerator register)
    {
        lyingSideIcon = register.generateIcon(getID() + "_lying_side.png");
        sideIcon = register.generateIcon(getID() + "_side.png");
        topAndBottomIcon = register.generateIcon(getID() + "_bottom_and_top.png");
    }

    public void onBlockAdded(World w, int x, int y, int z, EnumSide side, Entity placer)
    {
        super.onBlockAdded(w, x, y, z, side, placer);
        IBlockStateValue orientation = EnumLogBlockStates.UP;
        if(side == EnumSide.NORTH || side == EnumSide.SOUTH)
        {
            orientation = EnumLogBlockStates.LYING_NS;
        }
        else if(side == EnumSide.EAST || side == EnumSide.WEST)
        {
            orientation = EnumLogBlockStates.LYING_WE;
        }
        w.setBlockState(x, y, z, BlockStates.ORIENTATION, orientation);
    }
}
