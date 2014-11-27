package org.craft.items;

import org.craft.entity.*;
import org.craft.modding.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.utils.*;

public class Item implements IStackable
{

    private String            id;
    private int               uid;
    private AddonContainer<?> container;

    public Item(String id)
    {
        this.id = id;
    }

    @Override
    public String getId()
    {
        return (container == null ? "ourcraft" : container.getId()) + ":" + id;
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

    public AddonContainer<?> getContainer()
    {
        return container;
    }

    public void setContainer(AddonContainer<?> container)
    {
        this.container = container;
    }

}
