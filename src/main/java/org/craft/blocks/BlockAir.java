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

    public boolean shouldRender()
    {
        return false;
    }

    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return false;
    }

    public AABB getCollisionBox(World w, int x, int y, int z)
    {
        return null;
    }

    public boolean letLightGoThrough()
    {
        return true;
    }

    public boolean isSolid()
    {
        return false;
    }
}
