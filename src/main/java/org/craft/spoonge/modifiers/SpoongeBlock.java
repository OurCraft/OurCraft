package org.craft.spoonge.modifiers;

import java.util.*;

import org.craft.blocks.states.*;
import org.craft.modding.modifiers.*;
import org.craft.spoonge.text.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.block.BlockState;
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
    public Map<org.craft.blocks.states.BlockState, IBlockStateValue> getDefaultBlockState()
    {
        return null;
    }

    @Shadow
    public String getUnlocalizedID()
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
        SpoongeBlockState blockState = new SpoongeBlockState();
        blockState.setType(this);
        return blockState;
    }

    @Override
    public BlockState getStateFromDataValue(byte data)
    {
        SpoongeBlockState blockState = new SpoongeBlockState();
        blockState.setType(this);
        return blockState;
    }

    @Override
    public Translation getTranslation()
    {
        return SpoongeTranslations.get(getUnlocalizedID());
    }

    @Override
    public boolean getTickRandomly()
    {
        // TODO Implement
        return false;
    }

    @Override
    public void setTickRandomly(boolean tickRandomly)
    {
        // TODO Implement

    }
}
