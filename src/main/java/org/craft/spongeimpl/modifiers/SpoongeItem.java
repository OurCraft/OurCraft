package org.craft.spongeimpl.modifiers;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.item.*;

@BytecodeModifier("org.craft.items.Item")
public class SpoongeItem implements ItemType
{

    @Shadow
    @Override
    public String getId()
    {
        return null;
    }

    @Shadow
    @Override
    public int getMaxStackQuantity()
    {
        return 0;
    }

}
