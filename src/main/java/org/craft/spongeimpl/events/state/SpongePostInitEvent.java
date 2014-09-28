package org.craft.spongeimpl.events.state;

import org.spongepowered.api.*;
import org.spongepowered.api.event.state.*;

public class SpongePostInitEvent extends SpongeStateEvent implements PostInitializationEvent
{

    public SpongePostInitEvent(Game game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
