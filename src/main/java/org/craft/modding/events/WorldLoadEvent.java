package org.craft.modding.events;

import org.craft.*;
import org.craft.world.*;

public class WorldLoadEvent extends WorldEvent
{

    public WorldLoadEvent(OurCraftInstance ourCraft, World world)
    {
        super(ourCraft, world);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
