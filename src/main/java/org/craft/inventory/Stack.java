package org.craft.inventory;

import org.craft.items.*;
import org.spongepowered.api.inventory.*;

public class Stack implements ItemStack
{
    private static final long serialVersionUID = 441709013588165740L;

    private int               stackSize;
    private short             damage;
    private IStackable        stackable;
    private boolean           hasCustomMaxStackSize;
    private int               customMaxStackSize;

    public Stack(IStackable stackable)
    {
        this(stackable, 1);
    }

    public Stack(IStackable stackable, int size)
    {
        this(stackable, size, (short) 0);
    }

    public Stack(IStackable stackable, int size, short damage)
    {
        this.stackable = stackable;
        this.stackSize = size;
        this.damage = damage;

        if(this.damage < 0)
            this.damage = 0;
    }

    /**
     * Returns the object corresponding to the stack.
     */
    public IStackable getItem()
    {
        return stackable;
    }

    @Override
    public int compareTo(ItemStack o)
    {
        return Integer.compare(stackSize, o.getQuantity());
    }

    @Override
    public short getDamage()
    {
        return damage;
    }

    @Override
    public void setDamage(short damage)
    {
        this.damage = damage;
    }

    @Override
    public int getQuantity()
    {
        return stackSize;
    }

    @Override
    public void setQuantity(int quantity) throws IllegalArgumentException
    {
        this.stackSize = quantity;
    }

    @Override
    public int getMaxStackQuantity()
    {
        if(hasCustomMaxStackSize)
            return customMaxStackSize;
        return stackable.getMaxStackQuantity();
    }

    @Override
    public void setMaxStackQuantity(int quantity)
    {
        hasCustomMaxStackSize = true;
        customMaxStackSize = quantity;
    }
}
