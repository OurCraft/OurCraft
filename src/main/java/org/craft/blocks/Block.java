package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class Block
{

    static
    {
        normalCubeAABB = new AABB(Vector3.NULL.copy(), Vector3.get(1, 1, 1));
    }
    private static AABB normalCubeAABB;

    private String      id;
    private TextureIcon blockIcon;

    /**
     * Block constructor. Takes in an ID to identify the block
     */
    public Block(String id)
    {
        this.id = id;
    }

    public String getID()
    {
        return id;
    }

    /**
     * Returns the icon depending on the current block state (side, position, etc.)
     */
    public TextureIcon getBlockIcon(World world, int x, int y, int z, EnumSide side)
    {
        return blockIcon;
    }

    /**
     * Registers the required icons of the block.<br/>
     * Usage: RenderBlocks.blockMap uses this method to create block icons from assets/ourcraft/textures/blocks/
     * 
     * @see RenderBlocks
     */
    public void registerIcons(IconGenerator register)
    {
        blockIcon = register.generateIcon(id + ".png");
    }

    /**
     * Returns if given side is opaque
     */
    public boolean isSideOpaque(World w, int x, int y, int z, EnumSide side)
    {
        return true;
    }

    /**
     * Returns if given side should be rendered.<br/>
     * Usage: BlockTransparent uses it to check if adjacent blocks are the same and hide side if they are.
     */
    public boolean shouldSideBeRendered(World w, int x, int y, int z, EnumSide side)
    {
        return true;
    }

    /**
     * Returns if the block should be rendered.<br/>
     * Usage: Only returns false when called on BlockAir
     */
    public boolean shouldRender()
    {
        return true;
    }

    /**
     * Returns collision box depending on the current block state (side, position, etc.)
     */
    public AABB getCollisionBox(World w, int x, int y, int z)
    {
        Vector3 translation = Vector3.get(x, y, z);
        AABB result = normalCubeAABB.translate(translation);
        translation.dispose();
        return result;
    }

    /**
     * Returns selection box depending on the current block state (side, position, etc.)
     */
    public AABB getSelectionBox(World world, int x, int y, int z)
    {
        return getCollisionBox(world, x, y, z);
    }

    /**
     * Returns if block should be rendered in given pass
     */
    public boolean shouldRenderInPass(EnumRenderPass pass)
    {
        return pass == EnumRenderPass.NORMAL;
    }

    /**
     * Returns if block let light go through
     */
    public boolean letLightGoThrough()
    {
        return false;
    }

    /**
     * Called when a block is placed by an entity
     */
    public void onBlockAdded(World w, int x, int y, int z, EnumSide side, Entity placer)
    {
        w.clearStates(x, y, z);
    }
}
