package org.craft.spongeimpl.events.state;

import org.spongepowered.api.*;
import org.spongepowered.api.event.state.*;

public class SpongeServerStartingEvent extends SpongeStateEvent implements ServerStartingEvent
{

    public SpongeServerStartingEvent(Game game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
