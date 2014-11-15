package org.craft.blocks;

import java.util.*;

import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.items.*;
import org.craft.maths.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.utils.*;
import org.craft.world.*;

public class Block implements IStackable
{

    static
    {
        normalCubeAABB = new AABB(Vector3.NULL.copy(), Vector3.get(1, 1, 1));
    }
    protected static AABB normalCubeAABB;

    /**
     * The string id of this block
     */
    private String        id;

    /**
     * An id given at launch of the game used to identify the block.
     */
    private short         uniqueID;

    /**
     * Block constructor. Takes in an ID to identify the block
     */
    public Block(String id)
    {
        this.id = id;
    }

    /**
     * Returns the String id of this block
     */
    public String getRawID()
    {
        return id;
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
     * Called before a block is placed by an entity
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
    }

    /**
     * Sets the UID of this block
     * <br/>
     * UID: An id given at launch of the game used to identify the block.
     */
    void setUniqueID(short id)
    {
        this.uniqueID = id;
    }

    /**
     * Returns the UID of this block.<br/>
     * UID: An id given at launch of the game used to identify the block.
     */
    public short getUniqueID()
    {
        return uniqueID;
    }

    @Override
    public String getId()
    {
        return "ourcraft:" + getRawID();
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
        onBlockAdded(user.worldObj, x1, y1, z1, side, user);
        user.worldObj.setBlock(x1, y1, z1, this);
        user.worldObj.updateBlock(x1, y1, z1, false, null);
    }

    @Override
    public String getUnlocalizedID()
    {
        return "block." + getRawID();
    }

    /**
     * Called when a neighbor block is updated
     * @param visited : Positions already visited by a call on updateBlocks
     */
    public void onBlockUpdate(World world, int x, int y, int z, ArrayList<Vector3> visited)
    {
        ;
    }

    /**
     * Called when a neighbor block is updated
     * @param visited : Positions already visited by a call on updateBlocks
     */
    public void onBlockUpdateFromNeighbor(World world, int x, int y, int z, ArrayList<Vector3> visited)
    {
        onBlockUpdate(world, x, y, z, visited);
    }

    /**
     * Returns true if this cube is solid
     */
    public boolean isSolid()
    {
        return true;
    }

}
