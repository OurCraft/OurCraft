package org.craft.spongeimpl.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.util.event.callback.*;

public class SpongeServerStartedEvent extends SpongeStateEvent implements ServerStartedEvent
{

    public SpongeServerStartedEvent(OurCraftInstance game)
    {
        super(game);
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
