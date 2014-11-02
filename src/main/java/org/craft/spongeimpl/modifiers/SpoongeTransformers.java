package org.craft.spongeimpl.modifiers;

import org.craft.modding.modifiers.*;
import org.craft.spongeimpl.items.*;
import org.craft.spongeimpl.world.*;

public class SpoongeTransformers implements ASMTransformerPlugin
{

    public void registerModifiers(ModifierClassTransformer trans)
    {
        trans.addModifier(SpoongeBlock.class);
        trans.addModifier(SpoongeItem.class);
        trans.addModifier(SpoongeItemStack.class);
        trans.addModifier(SpoongeWorld.class); // TODO: Find a way to modify this class
    }
}