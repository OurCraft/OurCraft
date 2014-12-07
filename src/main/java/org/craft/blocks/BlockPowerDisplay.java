package org.craft.blocks;

import java.util.*;

import org.craft.blocks.states.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockPowerDisplay extends Block implements IPowerableBlock
{

    public BlockPowerDisplay(String id)
    {
        super(id);
    }

    @Override
    public void onBlockAdded(World w, int x, int y, int z, EnumSide side, Entity placer)
    {
        super.onBlockAdded(w, x, y, z, side, placer);
        w.setBlockState(x, y, z, BlockStates.powered, BlockStates.getValue(BlockStates.powered, "false"));
    }

    @Override
    public void onBlockUpdate(World world, int x, int y, int z, List<Vector3> visited)
    {
        int power = world.getDirectElectricPowerAt(x, y, z);
        world.setBlockState(x, y, z, BlockStates.powered, BlockStates.getValue(BlockStates.powered, power == 0 ? "false" : "true"));
    }

}
