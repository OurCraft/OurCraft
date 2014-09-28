package org.craft.spongeimpl.events.state;

import org.spongepowered.api.*;
import org.spongepowered.api.event.state.*;

public class SpongeInitEvent extends SpongeStateEvent implements InitializationEvent
{

    public SpongeInitEvent(Game game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
