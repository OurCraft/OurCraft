package org.craft;

import java.util.*;

import org.craft.blocks.*;
import org.craft.entity.*;
import org.craft.items.*;

public class GlobalRegistry
{

    public Block getBlockFromID(String id)
    {
        return Blocks.get(transformID(id));
    }

    public Item getItemFromID(String id)
    {
        return Items.get(transformID(id));
    }

    private String transformID(String id)
    {
        if(!id.contains(":"))
        {
            id = "ourcraft:" + id;
        }
        else if(id.startsWith("minecraft:"))
        {
            id = id.replace("minecraft:", "ourcraft:");
        }
        return id;
    }

    public List<Block> getAllBlocks()
    {
        return Blocks.getBlocks();
    }

    public List<Item> getAllItems()
    {
        return Items.getItems();
    }

    public void registerBlock(Block b)
    {
        Blocks.register(b);
    }

    public void registerItem(Item i)
    {
        Items.register(i);
    }

    public void registerEntityType(String id, Class<? extends Entity> e)
    {
        EntityRegistry.register(id, e);
    }

    public Class<? extends Entity> getEntityType(String id)
    {
        return EntityRegistry.get(id);
    }

    public String getEntityID(Class<? extends Entity> type)
    {
        return EntityRegistry.getType(type);
    }

    public String getEntityID(Entity e)
    {
        return EntityRegistry.getType(e);
    }

    public Collection<Class<? extends Entity>> getAllEntityTypes()
    {
        return EntityRegistry.getAllEntityTypes();
    }
}
