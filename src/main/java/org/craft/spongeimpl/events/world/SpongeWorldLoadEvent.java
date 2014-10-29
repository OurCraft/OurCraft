package org.craft.spongeimpl.events.world;

import org.craft.*;
import org.spongepowered.api.event.world.*;
import org.spongepowered.api.world.*;

public class SpongeWorldLoadEvent extends SpongeWorldEvent implements WorldLoadEvent
{

    public SpongeWorldLoadEvent(OurCraftInstance game, World world)
    {
        super(game, world);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
