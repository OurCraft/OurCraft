package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.util.*;
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

    public void registerIcons(IconGenerator register)
    {
        ;
    }

    public AABB getCollisionBox(World w, int x, int y, int z)
    {
        return null;
    }
}
