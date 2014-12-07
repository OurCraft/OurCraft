package org.craft.blocks;

import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockAir extends Block
{

    public BlockAir()
    {
        super("air");
    }

    @Override
    public boolean shouldRender()
    {
        return false;
    }

    @Override
    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return false;
    }

    @Override
    public AABB getCollisionBox(World w, int x, int y, int z)
    {
        return null;
    }

    @Override
    public boolean letLightGoThrough()
    {
        return true;
    }

    @Override
    public boolean isSolid()
    {
        return false;
    }
}
