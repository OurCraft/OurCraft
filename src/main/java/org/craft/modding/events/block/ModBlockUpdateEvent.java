package org.craft.modding.events.block;

import org.craft.*;
import org.craft.blocks.*;
import org.craft.world.*;

public class ModBlockUpdateEvent extends ModBlockEvent
{

    private Block causer;

    public ModBlockUpdateEvent(OurCraftInstance instance, World w, int x, int y, int z, Block block, Block causer)
    {
        super(instance, w, x, y, z, block);
        this.causer = causer;
    }

    public Block getCauseBlock()
    {
        return causer;
    }

    @Override
    public boolean isCancellable()
    {
        return true;
    }

}
