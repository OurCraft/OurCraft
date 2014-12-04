package org.craft.spoonge.events.block;

import org.craft.*;
import org.craft.spoonge.*;
import org.craft.spoonge.events.*;
import org.spongepowered.api.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.event.block.*;

public abstract class SpoongeBlockEvent extends SpoongeEvent implements BlockEvent
{

    private BlockLoc block;

    public SpoongeBlockEvent(OurCraftInstance instance, BlockLoc block)
    {
        super(instance);
        this.block = block;
    }

    @Override
    public Game getGame()
    {
        return SpoongeMod.instance;
    }

    @Override
    public BlockLoc getBlock()
    {
        return block;
    }

}
