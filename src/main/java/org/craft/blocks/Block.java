package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class Block
{

    static
    {
        normalCubeAABB = new AABB(Vector3.get(0, 0, 0), Vector3.get(1, 1, 1));
    }
    private static AABB normalCubeAABB;

    private String      id;
    private TextureIcon blockIcon;

    public Block(String id)
    {
        this.id = id;
    }

    public String getID()
    {
        return id;
    }

    public TextureIcon getBlockIcon(World world, int x, int y, int z, EnumSide side)
    {
        return blockIcon;
    }

    public void registerIcons(IconGenerator register)
    {
        blockIcon = register.generateIcon(id + ".png");
    }

    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return true;
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
        return normalCubeAABB.translate(Vector3.get(x, y, z));
    }

    public AABB getSelectionBox(World world, int x, int y, int z)
    {
        return getCollisionBox(world, x, y, z);
    }

    public boolean shouldRenderInPass(int pass)
    {
        return pass == 0;
    }

    public boolean letLightGoThrough()
    {
        return false;
    }
}
