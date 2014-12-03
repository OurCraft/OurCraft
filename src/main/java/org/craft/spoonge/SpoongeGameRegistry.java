package org.craft.spoonge;

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
        return com.google.common.base.Optional.of((ItemType) Items.get(id));
    }

    @Override
    public com.google.common.base.Optional<String> getId(Object obj)
    {
        if(obj instanceof BlockType)
        {
            return com.google.common.base.Optional.of(((BlockType) obj).getId());
        }
        else if(obj instanceof ItemType)
        {
            return com.google.common.base.Optional.of(((ItemType) obj).getId());
        }
        return null;
    }

}
