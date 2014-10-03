package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockHalfSlab extends Block
{

    private static AABB halfSlabBB;

    static
    {
        halfSlabBB = new AABB(Vector3.get(0, 0, 0), Vector3.get(1, 0.5f, 1));
    }

    private String      baseId;

    public BlockHalfSlab(String id)
    {
        super("slab_" + id);
        this.baseId = id;
    }

    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return side == EnumSide.BOTTOM;
    }

    public boolean shouldSideBeRendered(World w, int x, int y, int z, EnumSide side)
    {
        return true;
    }

    public boolean shouldRender()
    {
        return true;
    }

    public AABB getCollisionBox(World w, int x, int y, int z)
    {
        Vector3 translation = Vector3.get(x, y, z);
        AABB result = halfSlabBB.translate(translation);
        translation.dispose();
        return result;
    }

    public AABB getSelectionBox(World world, int x, int y, int z)
    {
        return getCollisionBox(world, x, y, z);
    }

    public TextureIcon getBlockIcon(World world, int x, int y, int z, EnumSide side)
    {
        return blockIcon;
    }

    public void registerIcons(IconGenerator register)
    {
        blockIcon = register.generateIcon(baseId + ".png");
    }
}
