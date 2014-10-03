package org.craft.blocks;

import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.items.*;
import org.craft.maths.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.utils.*;
import org.craft.world.*;
import org.spongepowered.api.block.*;

public class Block implements IStackable
{

    static
    {
        normalCubeAABB = new AABB(Vector3.NULL.copy(), Vector3.get(1, 1, 1));
    }
    protected static AABB normalCubeAABB;

    private String        id;
    protected TextureIcon blockIcon;

    private short         uniqueID;

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

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z)
    {
        // Log.message("UPDATE " + this.getID());
    }

    public void setUniqueID(short id)
    {
        this.uniqueID = id;
    }

    public short getUniqueID()
    {
        return uniqueID;
    }

    @Override
    public String getId()
    {
        return "ourcraft:" + getID();
    }

    @Override
    public int getMaxStackQuantity()
    {
        return 64;
    }

    @Override
    public void onUse(Entity user, float x, float y, float z, EnumSide side, CollisionType type)
    {
        if(type != CollisionType.BLOCK)
            return;
        int x1 = (int) (x + side.getTranslationX());
        int y1 = (int) (y + side.getTranslationY());
        int z1 = (int) (z + side.getTranslationZ());
        user.worldObj.setBlock(x1, y1, z1, this);
        onBlockAdded(user.worldObj, x1, y1, z1, side, user);
    }

    @Override
    public String getUnlocalizedID()
    {
        return "block." + getID();
    }

    public void onBlockUpdate(World world, int x, int y, int z)
    {
        ;
    }

    public void onBlockUpdateFromNeighbor(World world, int x, int y, int z)
    {
        onBlockUpdate(world, x, y, z);
    }

    public boolean isSolid()
    {
        return true;
    }

    @Override
    public BlockType getBlock()
    {
        return null;
    }

}
