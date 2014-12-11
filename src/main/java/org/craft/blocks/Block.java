package org.craft.blocks;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.states.*;
import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.items.*;
import org.craft.maths.*;
import org.craft.modding.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.utils.*;
import org.craft.world.*;

public class Block implements IStackable
{

    static
    {
        normalCubeAABB = new AABB(Vector3.NULL.copy(), Vector3.get(1, 1, 1));
    }
    protected static AABB     normalCubeAABB;

    /**
     * The string id of this block
     */
    private String            id;

    /**
     * An id given at launch of the game used to identify the block.
     */
    private short             uniqueID;

    private AddonContainer<?> container;

    private float             explosionResistance;

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
        w.removeScheduledUpdater(x, y, z);
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
        return (container == null ? "ourcraft" : container.getId()) + ":" + id;
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
        user.worldObj.updateBlock(x1, y1, z1, false, null, user.worldObj.getBlockAt(x1, y1, z1));
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
    public void onBlockUpdate(World world, int x, int y, int z, List<Vector3> visited)
    {
        ;
    }

    /**
     * Called when a neighbor block is updated
     * @param visited : Positions already visited by a call on updateBlocks
     */
    public void onBlockUpdateFromNeighbor(World world, int x, int y, int z, List<Vector3> visited)
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

    public void onScheduledUpdate(World world, int x, int y, int z, long interval, long tick)
    {

    }

    /**
     * Method called when the player performs a right click on this block.<br/>
     * Returns <code>true</code> if the block performs anything on click, returns <code>false</code> otherwise.
     * @param world The World in which the event occurred
     * @param x Coordinate of X axis of the event
     * @param y Coordinate of Y axis of the event
     * @param z Coordinate of Z axis of the event
     * @param player The player who created the event, Warning: Might be <code>null</code>!!
     * @param heldStack The item stack held by the player, might be null!
     * @return true if the block performs an action when clicking on it and cancels block placement if any scheduled. false otherwise.
     */
    public boolean onBlockClicked(World world, int x, int y, int z, EntityPlayer player, org.craft.inventory.Stack heldStack)
    {
        return false;
    }

    public AddonContainer<?> getContainer()
    {
        return container;
    }

    public void setContainer(AddonContainer<?> container)
    {
        this.container = container;
    }

    public Map<BlockState, IBlockStateValue> getDefaultBlockState()
    {
        return Maps.newHashMap();
    }

    public float getExplosionResistance()
    {
        return explosionResistance;
    }

    public Block setExplosionResistance(float r)
    {
        this.explosionResistance = r;
        return this;
    }

    public void onDestroyedByExplosion(Explosion explosion, World world, int blockX, int blockY, int blockZ)
    {
        ;
    }
}
