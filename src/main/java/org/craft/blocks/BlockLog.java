package org.craft.blocks;

import org.craft.blocks.states.*;
import org.craft.entity.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockLog extends Block
{

    public BlockLog(String id)
    {
        super(id);
    }

    @Override
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
        w.setBlockState(x, y, z, BlockStates.logOrientation, orientation);
    }


}
