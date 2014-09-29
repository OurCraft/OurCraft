package org.craft.spongeimpl.events.state;

import org.spongepowered.api.*;
import org.spongepowered.api.event.state.*;

public class SpongeServerStoppedEvent extends SpongeStateEvent implements ServerStoppedEvent
{

    public SpongeServerStoppedEvent(Game game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
