package org.craft.spongeimpl.game;

import org.craft.blocks.*;
import org.craft.items.*;
import org.spongepowered.api.*;

public class SpongeGameRegistry implements GameRegistry
{

    @Override
    public Block getBlock(String id)
    {
        return Blocks.get(id);
    }

    @Override
    public Item getItem(String id)
    {
        return Items.get(id);
    }

    @Override
    public String getID(Object obj)
    {
        if(obj instanceof IStackable)
        {
            return ((IStackable) obj).getID();
        }
        return null;
    }

    public void registerBlock(Block block)
    {
        Blocks.register(block);
    }

    public void registerItem(Item item)
    {
        Items.register(item);
    }
}
