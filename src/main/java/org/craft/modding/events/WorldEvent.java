package org.craft.modding.events;

import org.craft.*;
import org.craft.world.*;

public class WorldEvent extends ModEvent
{

    private World world;

    public WorldEvent(OurCraftInstance ourcraft, World world)
    {
        super(ourcraft);
        this.world = world;
    }

    public World getWorld()
    {
        return world;
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
