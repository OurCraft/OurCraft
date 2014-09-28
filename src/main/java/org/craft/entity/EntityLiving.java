package org.craft.entity;

import org.craft.world.*;
import org.spongepowered.api.entity.*;

public class EntityLiving extends Entity implements LivingEntity
{

    private double health;

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

}
