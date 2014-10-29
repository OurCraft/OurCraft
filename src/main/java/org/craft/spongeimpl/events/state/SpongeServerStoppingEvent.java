package org.craft.spongeimpl.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpongeServerStoppingEvent extends SpongeStateEvent implements ServerStoppingEvent
{

    public SpongeServerStoppingEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
