package org.craft.spongeimpl.events.state;

import org.craft.spongeimpl.events.*;
import org.spongepowered.api.*;
import org.spongepowered.api.event.state.*;

public abstract class SpongeStateEvent extends SpongeGameEvent implements StateEvent
{

    public SpongeStateEvent(Game game)
    {
        super(game);
    }

}
