package org.craft.spongeimpl.items;

import org.craft.inventory.*;
import org.craft.modding.modifiers.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.item.*;
import org.spongepowered.api.item.inventory.*;

@BytecodeModifier("org.craft.inventory.Stack")
public class SpoongeItemStack implements ItemStack, ItemBlock
{

    private static final long serialVersionUID = -747859447420118785L;

    @Shadow
    public int compareTo(Stack s)
    {
        return 0;
    }

    @Override
    public int compareTo(ItemStack o)
    {
        return compareTo((Stack) o);
    }

    @Override
    public ItemType getItem()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public short getDamage()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setDamage(short damage)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getQuantity()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setQuantity(int quantity) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getMaxStackQuantity()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMaxStackQuantity(int quantity)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String getId()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockType getBlock()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
