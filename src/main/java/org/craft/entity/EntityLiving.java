package org.craft.entity;

import com.mojang.nbt.*;

import org.craft.inventory.*;
import org.craft.world.*;

public abstract class EntityLiving extends Entity
{

    private double       health;
    protected IInventory inventory;

    public EntityLiving(World world)
    {
        super(world);
    }

    public void damage(double amount)
    {
        health -= amount;
    }

    public double getHealth()
    {
        return health;
    }

    public void setHealth(double health)
    {
        this.health = health;
    }

    /**
     * Returns entity's inventory.<br/>Null by default and should be assigned a value by classes extending EntityLiving
     */
    public IInventory getInventory()
    {
        return inventory;
    }

    /**
     * Returns currently held item by the entity
     */
    public org.craft.inventory.Stack getHeldItem()
    {
        return null;
    }

    @Override
    public void readFromNBT(NBTCompoundTag compound)
    {
        super.readFromNBT(compound);
        health = compound.getDouble("health");
    }

    @Override
    public void writeToNBT(NBTCompoundTag compound)
    {
        super.writeToNBT(compound);
        compound.putDouble("health", health);
    }

}
