package org.craft.items;

import java.util.*;

public class Items
{

    public static final HashMap<String, Item> ITEM_REGISTRY = new HashMap<String, Item>();
    private static ArrayList<Item>            itemByID;
    public static Item                        test;
    public static Item                        test2;

    public static void init()
    {
        itemByID = new ArrayList<Item>();

        register(test = new Item("test"));
        register(test2 = new Item("test2"));

        for(short i = 0; i < itemByID.size(); i++ )
        {
            Item b = itemByID.get(i);
            if(b != null)
                b.setUID(i);
        }
    }

    /**
     * Registers a block into the ITEM_REGISTRY field
     */
    public static void register(Item item)
    {
        if(ITEM_REGISTRY.containsKey(item.getID()))
        {
            throw new IllegalArgumentException("Id " + item.getID() + " is already used by " + ITEM_REGISTRY.get(item.getID()) + " when trying to add " + item);
        }
        ITEM_REGISTRY.put(item.getID(), item);
        itemByID.add(item);
    }

    /**
     * Returns the block in ITEM_REGISTRY with given id
     */
    public static Item get(String string)
    {
        if(string == null)
            return null;
        return ITEM_REGISTRY.get(string);
    }

    public static Item getByID(int id)
    {
        return itemByID.get(id);
    }
}
