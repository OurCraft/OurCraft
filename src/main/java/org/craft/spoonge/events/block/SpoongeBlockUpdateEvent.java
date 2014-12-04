package org.craft.spoonge.events.block;

import com.google.common.base.*;

import org.craft.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.event.block.*;
import org.spongepowered.api.event.cause.*;

public class SpoongeBlockUpdateEvent extends SpoongeBlockEvent implements BlockUpdateEvent
{

    private BlockType causer;

    public SpoongeBlockUpdateEvent(OurCraftInstance instance, BlockLoc block, BlockType causer)
    {
        super(instance, block);
        this.causer = causer;
    }

    @Override
    public Optional<Cause> getCause()
    {
        return Optional.absent();
    }

    @Override
    public BlockType getCauseBlockType()
    {
        return causer;
    }

    @Override
    public boolean isCancellable()
    {
        return true;
    }

}
