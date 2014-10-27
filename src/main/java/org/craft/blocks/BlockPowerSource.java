package org.craft.blocks;

import org.craft.blocks.states.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockPowerSource extends Block implements IPowerableBlock
{

    private static AABB aabb;

    static
    {
        aabb = new AABB(Vector3.get(4f / 16f, 0f, 4f / 16f), Vector3.get(12f / 16f, 14f / 16f, 12f / 16f));
    }

    public BlockPowerSource(String id)
    {
        super(id);
    }

    public void onBlockAdded(World w, int x, int y, int z, EnumSide side, Entity placer)
    {
        super.onBlockAdded(w, x, y, z, side, placer);
        w.setBlockState(x, y, z, BlockStates.electricPower, EnumPowerStates.POWER_15);
    }

    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return false;
    }

    public AABB getCollisionBox(World w, int x, int y, int z)
    {
        Vector3 translation = Vector3.get(x, y, z);
        AABB result = aabb.translate(translation);
        translation.dispose();
        return result;
    }
}
