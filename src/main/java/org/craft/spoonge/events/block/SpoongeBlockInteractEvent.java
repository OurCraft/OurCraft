package org.craft.spoonge.events.block;

import com.google.common.base.*;

import org.craft.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.event.block.*;
import org.spongepowered.api.event.cause.*;

public class SpoongeBlockInteractEvent extends SpoongeBlockEvent implements BlockInteractEvent
{

    public SpoongeBlockInteractEvent(OurCraftInstance instance, BlockLoc block)
    {
        super(instance, block);
    }

    @Override
    public Optional<Cause> getCause()
    {
        return Optional.absent();
    }

    @Override
    public boolean isCancellable()
    {
        return true;
    }

}
