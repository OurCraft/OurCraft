package org.craft.blocks;

import org.craft.blocks.states.*;
import org.craft.entity.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockPowerSource extends Block
{

    public BlockPowerSource(String id)
    {
        super(id);
    }

    public void onBlockAdded(World w, int x, int y, int z, EnumSide side, Entity placer)
    {
        super.onBlockAdded(w, x, y, z, side, placer);
        w.setBlockState(x, y, z, BlockStates.electricPower, EnumPowerStates.POWER_15);
    }
}
