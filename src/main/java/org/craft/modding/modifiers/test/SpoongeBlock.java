package org.craft.modding.modifiers.test;

import org.craft.modding.modifiers.*;
import org.craft.spongeimpl.*;
import org.spongepowered.api.block.*;

@BytecodeModifier(Block.class)
public class SpoongeBlock implements BlockType
{

    @Override
    public String getId()
    {
        return SpoongeMod.instance.getRegistry().getId(this).get();
    }

}
