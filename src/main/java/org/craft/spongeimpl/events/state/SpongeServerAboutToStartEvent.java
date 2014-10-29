package org.craft.spongeimpl.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpongeServerAboutToStartEvent extends SpongeStateEvent implements ServerAboutToStartEvent
{

    public SpongeServerAboutToStartEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
