package org.craft.spoonge.items;

import java.util.*;

import org.craft.blocks.*;
import org.craft.items.*;
import org.craft.modding.modifiers.*;
import org.craft.spoonge.text.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.item.*;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.text.translation.*;

@BytecodeModifier("org.craft.inventory.Stack")
public class SpoongeItemStack implements ItemStack, ItemBlock
{

    private static final long serialVersionUID = -747859447420118785L;

    //===========================================================
    //               START OF SHADOW METHODS
    //===========================================================
    @Shadow
    public int compareTo(org.craft.inventory.Stack s)
    {
        return 0;
    }

    @Shadow
    public IStackable getStackable()
    {
        return null;
    }

    @Shadow
    @Override
    public short getDamage()
    {
        return 0;
    }

    @Shadow
    @Override
    public void setDamage(short damage)
    {
        ;
    }

    @Shadow
    @Override
    public int getQuantity()
    {
        return 0;
    }

    @Shadow
    @Override
    public void setQuantity(int quantity) throws IllegalArgumentException
    {
        ;
    }

    @Shadow
    @Override
    public int getMaxStackQuantity()
    {
        return 0;
    }

    @Shadow
    @Override
    public void setMaxStackQuantity(int quantity)
    {
        ;
    }

    //===========================================================
    //               END OF SHADOW METHODS
    //===========================================================

    @Override
    public int compareTo(ItemStack o)
    {
        return compareTo((org.craft.inventory.Stack) o);
    }

    @Override
    public ItemType getItem()
    {
        if(getStackable() instanceof Item)
        {
            return (ItemType) getStackable();
        }
        return null;
    }

    @Override
    public String getId()
    {
        return getStackable().getId();
    }

    @Override
    public BlockType getBlock()
    {
        if(getStackable() instanceof Block)
        {
            return (BlockType) getStackable();
        }
        return null;
    }

    @Override
    public Translation getTranslation()
    {
        return SpoongeTranslations.get(getStackable().getUnlocalizedID());
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEnchanted()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setEnchantment(Enchantment enchant, int level)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeEnchantment(Enchantment enchant)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getEnchantment(Enchantment enchant)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
