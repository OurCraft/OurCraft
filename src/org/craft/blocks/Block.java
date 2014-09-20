package org.craft.blocks;

import java.util.*;

import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class Block
{

    public static final HashMap<String, Block> registry = new HashMap<>();
    private String                             id;
    private TextureIcon                        blockIcon;
    private static AABB                        normalCubeAABB;

    public Block(String id)
    {
        if(registry.containsKey(id))
        {
            throw new IllegalArgumentException("Id " + id + " is already used by " + registry.get(id) + " when trying to add " + this);
        }
        this.id = id;
        registry.put(id, this);
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

    public boolean shouldRender()
    {
        return true;
    }

    public AABB getCollisionBox(World w, int x, int y, int z)
    {
        return normalCubeAABB.translate(Vector3.get(x, y, z));
    }

    static
    {
        normalCubeAABB = new AABB(Vector3.get(0, 0, 0), Vector3.get(1, 1, 1));
    }

    public AABB getSelectionBox(World world, int x, int y, int z)
    {
        return getCollisionBox(world, x, y, z);
    }
}
