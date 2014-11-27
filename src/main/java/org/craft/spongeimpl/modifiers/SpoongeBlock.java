package org.craft.spongeimpl.modifiers;

import org.craft.modding.modifiers.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.text.translation.*;

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
    public String shadow$id;

    //===========================================================
    //               END OF SHADOW METHODS
    //===========================================================

    @Override
    public BlockState getDefaultState()
    {
        return null;
    }

    @Override
    public BlockState getStateFromDataValue(byte data)
    {
        return null;
    }

    @Override
    public Translation getTranslation()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
