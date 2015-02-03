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

    @Override
    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return false;
    }

    @Override
    public boolean shouldRenderInPass(EnumRenderPass pass)
    {
        return pass == EnumRenderPass.ALPHA;
    }

    @Override
    public boolean shouldSideBeRendered(World w, int x, int y, int z, EnumSide side)
    {
        Block next = w.getBlockNextTo(x, y, z, side);
        return next != this;
    }

    @Override
    public boolean letLightGoThrough()
    {
        return true;
    }

    @Override
    public EnumRenderPass getDefaultRenderPass()
    {
        return EnumRenderPass.ALPHA;
    }
}
