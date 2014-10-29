package org.craft.spongeimpl.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpongeServerStartingEvent extends SpongeStateEvent implements ServerStartingEvent
{

    public SpongeServerStartingEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
