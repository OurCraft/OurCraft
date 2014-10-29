package org.craft.spongeimpl;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.blocks.Block;
import org.craft.items.*;
import org.spongepowered.api.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.item.*;

public class SpoongeGameRegistry implements GameRegistry
{

    private HashMap<String, BlockType> blockTypes;
    private HashMap<String, ItemType>  itemTypes;

    public SpoongeGameRegistry()
    {
    }

    public void init()
    {
        blockTypes = Maps.newHashMap();
        for(final Block b : Blocks.BLOCK_REGISTRY.values())
        {
            blockTypes.put(b.getId(), new BlockType()
            {

                @Override
                public String getId()
                {
                    return b.getId();
                }
            });
        }

        itemTypes = Maps.newHashMap();
        for(final Item i : Items.ITEM_REGISTRY.values())
        {
            itemTypes.put(i.getId(), new ItemType()
            {

                @Override
                public String getId()
                {
                    return i.getId();
                }

                @Override
                public int getMaxStackQuantity()
                {
                    return i.getMaxStackQuantity();
                }
            });
        }
    }

    @Override
    public com.google.common.base.Optional<BlockType> getBlock(String id)
    {
        return com.google.common.base.Optional.of(blockTypes.get(id));
    }

    @Override
    public com.google.common.base.Optional<ItemType> getItem(String id)
    {
        return com.google.common.base.Optional.of(itemTypes.get(id));
    }

    @Override
    public com.google.common.base.Optional<String> getId(Object obj)
    {
        // TODO: Be more precise Sponge on what obj can be, that'll help
        return null;
    }

}
