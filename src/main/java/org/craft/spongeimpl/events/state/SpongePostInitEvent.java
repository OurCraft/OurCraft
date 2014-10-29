package org.craft.spongeimpl.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpongePostInitEvent extends SpongeStateEvent implements PostInitializationEvent
{

    public SpongePostInitEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
