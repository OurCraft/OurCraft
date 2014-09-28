package org.craft.entity;

import org.craft.blocks.*;
import org.craft.inventory.*;
import org.craft.world.*;
import org.spongepowered.api.entity.*;

public class EntityLiving extends Entity implements LivingEntity
{

    private double       health;
    protected IInventory inventory;

    public EntityLiving(World world)
    {
        super(world);
    }

    @Override
    public void damage(double amount)
    {
        health -= amount;
    }

    @Override
    public double getHealth()
    {
        return health;
    }

    @Override
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
    public Stack getHeldItem()
    {
        return null;
    }

}
