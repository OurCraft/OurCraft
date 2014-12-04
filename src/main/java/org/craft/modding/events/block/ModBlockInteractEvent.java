package org.craft.modding.events.block;

import org.craft.*;
import org.craft.blocks.*;
import org.craft.world.*;

public class ModBlockInteractEvent extends ModBlockEvent
{

    public ModBlockInteractEvent(OurCraftInstance instance, World world, int x, int y, int z, Block block)
    {
        super(instance, world, x, y, z, block);
    }

    @Override
    public boolean isCancellable()
    {
        return true;
    }

}
