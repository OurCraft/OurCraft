package org.craft.spongeimpl.modifiers;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.item.*;
import org.spongepowered.api.text.translation.*;

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

    @Override
    public Translation getTranslation()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
