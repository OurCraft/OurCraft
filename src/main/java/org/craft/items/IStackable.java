package org.craft.items;

import org.craft.entity.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.utils.*;

public interface IStackable
{

    /**
     * Gets the id of this item.
     *
     * <p>Ex. Minecraft registers a golden carrot as
     * "minecraft:golden_carrot".</p>
     *
     * @return The id
     */
    String getId();

    /**
     * Get the default maximum quantity for ItemStacks
     * of this item.
     *
     * @return Max stack quantity
     */
    int getMaxStackQuantity();

    /**
     * Defines the behavior of this stackable instance when used
     */
    void onUse(Entity user, float x, float y, float z, EnumSide side, CollisionType type);

    /**
     * Returns an unlocalized string depending on the object's id
     */
    String getUnlocalizedID();
}
