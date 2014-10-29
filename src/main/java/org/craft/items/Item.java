package org.craft.items;

import org.craft.entity.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.utils.*;

public class Item implements IStackable
{

    private String id;
    private int    uid;

    public Item(String id)
    {
        this.id = id;
    }

    @Override
    public String getId()
    {
        return "ourcraft:" + id;
    }

    @Override
    public int getMaxStackQuantity()
    {
        return 64;
    }

    public void setUID(int uid)
    {
        this.uid = uid;
    }

    public int getUID()
    {
        return uid;
    }

    @Override
    public void onUse(Entity user, float x, float y, float z, EnumSide side, CollisionType type)
    {
        ;
    }

    @Override
    public String getUnlocalizedID()
    {
        return "item." + id;
    }
}
