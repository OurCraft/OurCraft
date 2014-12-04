package org.craft.spoonge.events.block;

import com.google.common.base.*;

import org.craft.*;
import org.craft.spoonge.*;
import org.spongepowered.api.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.event.block.*;
import org.spongepowered.api.event.cause.*;

public class SpoongeBlockChangeEvent extends SpoongeBlockEvent implements BlockChangeEvent
{

    private BlockSnapshot newBlock;

    public SpoongeBlockChangeEvent(OurCraftInstance instance, BlockLoc oldBlock, BlockSnapshot newBlock)
    {
        super(instance, oldBlock);
        this.newBlock = newBlock;
    }

    @Override
    public Optional<Cause> getCause()
    {
        return Optional.absent();
    }

    @Override
    public BlockSnapshot getReplacementBlock()
    {
        return newBlock;
    }

    @Override
    public boolean isCancellable()
    {
        return true;
    }

    @Override
    public Game getGame()
    {
        return SpoongeMod.instance;
    }

}
