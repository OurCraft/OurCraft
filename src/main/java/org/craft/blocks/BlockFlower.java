package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class BlockFlower extends Block
{

    private static AABB flowerBB;

    static
    {
        flowerBB = new AABB(Vector3.get(0.25f, 0f, 0.25f), Vector3.get(0.75f, 1f, 0.75f));
    }

    public BlockFlower(String id)
    {
        super(id);
    }

    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return false;
    }

    public AABB getCollisionBox(World w, int x, int y, int z)
    {
        return null;
    }

    /**
     * Returns selection box depending on the current block state (side, position, etc.)
     */
    public AABB getSelectionBox(World world, int x, int y, int z)
    {
        Vector3 translation = Vector3.get(x, y, z);
        AABB result = flowerBB.translate(translation);
        translation.dispose();
        return result;
    }

    public boolean shouldRenderInPass(EnumRenderPass pass)
    {
        return pass == EnumRenderPass.ALPHA;
    }

    public void onBlockUpdate(World world, int x, int y, int z)
    {
        if(!world.getBlockAt(x, y - 1, z).isSolid())
        {
            world.setBlock(x, y, z, Blocks.air);
        }
    }

    public boolean isSolid()
    {
        return false;
    }
}
