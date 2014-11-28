package org.craft.spongeimpl.events.world;

import org.craft.*;
import org.spongepowered.api.event.world.*;
import org.spongepowered.api.util.event.callback.*;
import org.spongepowered.api.world.*;

public class SpongeWorldUnloadEvent extends SpongeWorldEvent implements WorldUnloadEvent
{

    public SpongeWorldUnloadEvent(OurCraftInstance game, World world)
    {
        super(game, world);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

    @Override
    public CallbackList getCallbacks()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
