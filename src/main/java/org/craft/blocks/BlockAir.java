package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockAir extends Block
{

    public BlockAir()
    {
        super("air");
    }

    public TextureIcon getBlockIcon(World w, int x, int y, int z, EnumSide side)
    {
        return null;
    }

    public boolean shouldRender()
    {
        return false;
    }

    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return false;
    }

    public void registerIcons(IconGenerator register)
    {
        ;
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
