package org.craft.spoonge.modifiers;

import org.craft.modding.modifiers.*;
import org.craft.spoonge.text.*;
import org.spongepowered.api.item.*;
import org.spongepowered.api.text.translation.*;

@BytecodeModifier("org.craft.items.Item")
public class SpoongeItem implements ItemType
{

    @Shadow
    public String getUnlocalizedID()
    {
        return null;
    }

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
        return SpoongeTranslations.get(getUnlocalizedID());
    }

}
