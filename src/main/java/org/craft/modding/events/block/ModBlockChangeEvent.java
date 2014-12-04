package org.craft.modding.events.block;

import org.craft.*;
import org.craft.blocks.*;
import org.craft.world.*;

public class ModBlockChangeEvent extends ModBlockEvent
{

    private Block newBlock;

    public ModBlockChangeEvent(OurCraftInstance instance, World w, int x, int y, int z, Block oldBlock, Block newBlock)
    {
        super(instance, w, x, y, z, oldBlock);
        this.newBlock = newBlock;
    }

    public Block getReplacementBlock()
    {
        return newBlock;
    }

    @Override
    public boolean isCancellable()
    {
        return true;
    }

}
