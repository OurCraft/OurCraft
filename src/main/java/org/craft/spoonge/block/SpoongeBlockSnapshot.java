package org.craft.spoonge.block;

import org.spongepowered.api.block.*;

public class SpoongeBlockSnapshot implements BlockSnapshot
{

    private BlockState state;

    public SpoongeBlockSnapshot(BlockState state)
    {
        this.state = state;
    }

    @Override
    public BlockState getState()
    {
        return state;
    }

}
