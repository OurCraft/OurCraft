package org.craft.items;

import org.craft.entity.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.utils.*;
import org.spongepowered.api.item.*;

public interface IStackable extends org.spongepowered.api.item.ItemType, ItemBlock
{

    /**
     * Defines the behavior of this stackable instance when used
     */
    void onUse(Entity user, float x, float y, float z, EnumSide side, CollisionType type);

    /**
     * Returns an unlocalized string depending on the object's id
     */
    String getUnlocalizedID();
}
