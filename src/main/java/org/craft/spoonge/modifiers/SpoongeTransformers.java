package org.craft.spoonge.modifiers;

import org.craft.modding.modifiers.*;
import org.craft.spoonge.items.*;
import org.craft.spoonge.service.command.*;

public class SpoongeTransformers implements ASMTransformerPlugin
{

    @Override
    public void registerModifiers(ModifierClassTransformer trans)
    {
        trans.addModifier(SpoongeMessages.class);
        trans.addModifier(SpoongeTextStyles.class);
        trans.addModifier(SpoongeTitles.class);
        trans.addModifier(SpoongeItemStack.class);
        trans.addModifier(SpoongeBlock.class);
        trans.addModifier(SpoongeItem.class);
        trans.addModifier(SpoongeTitle.class);
        trans.addModifier(SpoongeWorld.class);
        trans.addModifier(SpoongeCommandMapping.class);
        trans.addModifier(SpoongeCommandsDispatcher.class);
    }
}
