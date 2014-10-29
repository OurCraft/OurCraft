package org.craft.spongeimpl.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

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

}
