package org.craft.spoonge;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.items.*;
import org.spongepowered.api.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.item.*;

public class SpoongeGameRegistry implements GameRegistry
{

    public SpoongeGameRegistry()
    {
    }

    @Override
    public com.google.common.base.Optional<BlockType> getBlock(String id)
    {
        if(!id.contains(":"))
        {
            id = "ourcraft:" + id;
        }
        else if(id.startsWith("minecraft:"))
        {
            id = id.replace("minecraft:", "ourcraft:");
        }
        return com.google.common.base.Optional.of((BlockType) Blocks.get(id));
    }

    @Override
    public com.google.common.base.Optional<ItemType> getItem(String id)
    {
        if(!id.contains(":"))
        {
            id = "ourcraft:" + id;
        }
        else if(id.startsWith("minecraft:"))
        {
            id = id.replace("minecraft:", "ourcraft:");
        }
        return com.google.common.base.Optional.of((ItemType) Items.get(id));
    }

    @Override
    public List<BlockType> getBlocks()
    {
        List<BlockType> types = Lists.newArrayList();
        for(Block block : Blocks.getBlocks())
            types.add((BlockType) block);
        return types;
    }

    @Override
    public List<ItemType> getItems()
    {
        List<ItemType> types = Lists.newArrayList();
        for(Item item : Items.getItems())
            types.add((ItemType) item);
        return types;
    }

}
