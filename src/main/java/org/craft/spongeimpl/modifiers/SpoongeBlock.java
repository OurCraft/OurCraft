package org.craft.spongeimpl.modifiers;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.block.*;

@BytecodeModifier("org.craft.blocks.Block")
public class SpoongeBlock implements BlockType
{

    @Shadow
    @Override
    public String getId()
    {
        return null;
    }

}
