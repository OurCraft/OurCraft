package org.craft.modding.events;

import org.craft.*;
import org.craft.world.*;

public class WorldUnloadEvent extends WorldEvent
{

    public WorldUnloadEvent(OurCraftInstance ourcraft, World clientWorld)
    {
        super(ourcraft, clientWorld);
    }

}
