package org.craft.blocks;

import org.craft.utils.*;
import org.craft.world.*;

public class BlockLeaves extends Block
{

    public BlockLeaves(String id)
    {
        super(id);
    }

    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return false;
    }

    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }

    public boolean shouldSideBeRendered(World w, int x, int y, int z, EnumSide side)
    {
        Block next = w.getBlockNextTo(x, y, z, side);
        return next != this;
    }
}
