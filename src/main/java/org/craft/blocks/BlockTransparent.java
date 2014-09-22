package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockTransparent extends Block
{

    public BlockTransparent(String id)
    {
        super(id);
    }

    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return false;
    }

    public boolean shouldRenderInPass(EnumRenderPass pass)
    {
        return pass == EnumRenderPass.ALPHA;
    }

    public boolean shouldSideBeRendered(World w, int x, int y, int z, EnumSide side)
    {
        Block next = w.getBlockNextTo(x, y, z, side);
        return next != this;
    }

    public boolean letLightGoThrough()
    {
        return true;
    }
}
