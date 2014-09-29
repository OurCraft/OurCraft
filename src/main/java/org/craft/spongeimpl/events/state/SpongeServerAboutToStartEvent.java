package org.craft.spongeimpl.events.state;

import org.spongepowered.api.*;
import org.spongepowered.api.event.state.*;

public class SpongeServerAboutToStartEvent extends SpongeStateEvent implements ServerAboutToStartEvent
{

    public SpongeServerAboutToStartEvent(Game game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
