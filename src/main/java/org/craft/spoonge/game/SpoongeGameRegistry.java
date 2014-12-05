package org.craft.spoonge.game;

import java.util.List;

import com.google.common.base.*;

import org.craft.blocks.*;
import org.craft.items.*;
import org.spongepowered.api.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.item.*;

public class SpoongeGameRegistry implements GameRegistry
{

    @Override
    public Optional<BlockType> getBlock(String id)
    {
        return Optional.of((BlockType) Blocks.get(id));
    }

    @Override
    public Optional<ItemType> getItem(String id)
    {
        return Optional.of((ItemType) Items.get(id));
    }

    public void registerBlock(Block block)
    {
        Blocks.register(block);
    }

    public void registerItem(Item item)
    {
        Items.register(item);
    }

    @Override
    public List<BlockType> getBlocks()
    {
        // TODO ASM IT
        return (List<BlockType>)Blocks.getBlocks();
    }

    @Override
    public List<ItemType> getItems()
    {
        // TODO Auto-generated method stub
        return (List<ItemType>)Items.getItems();
    }
}
