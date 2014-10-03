package org.craft.spongeimpl.game;

import com.google.common.base.*;

import org.craft.blocks.*;
import org.craft.blocks.Block;
import org.craft.items.*;
import org.spongepowered.api.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.item.*;

public class SpongeGameRegistry implements GameRegistry
{

    @Override
    public Optional<BlockType> getBlock(String id)
    {
        // TODO
        Blocks.get(id);
        return null;
    }

    @Override
    public Optional<ItemType> getItem(String id)
    {
        // TODO
        Items.get(id);
        return null;
    }

    @Override
    public Optional<String> getId(Object obj)
    {
        if(obj instanceof IStackable)
        {
            // TODO
            ((IStackable) obj).getId();
            return null;
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
