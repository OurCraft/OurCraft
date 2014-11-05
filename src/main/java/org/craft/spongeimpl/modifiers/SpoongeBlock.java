package org.craft.spongeimpl.modifiers;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.block.*;

@BytecodeModifier("org.craft.blocks.Block")
public class SpoongeBlock implements BlockType
{

    //===========================================================
    //               START OF SHADOW METHODS
    //===========================================================
    @Shadow
    @Override
    public String getId()
    {
        return null;
    }
    
    @Shadow
    @Override
    public BlockState getDefaultState()
    {
        return null;
    }

    @Shadow
    @Override
    public BlockState getStateFromDataValue(byte data)
    {
        return null;
    }

    @Shadow
    public String shadow$id;

    //===========================================================
    //               END OF SHADOW METHODS
    //===========================================================

}
