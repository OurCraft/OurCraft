package org.craft.spongeimpl.events.state;

import org.craft.*;
import org.craft.spongeimpl.events.*;
import org.spongepowered.api.event.state.*;

public abstract class SpongeStateEvent extends SpongeGameEvent implements StateEvent
{

    public SpongeStateEvent(OurCraftInstance game)
    {
        super(game);
    }

}
