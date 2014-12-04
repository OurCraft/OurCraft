package org.craft.spoonge.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpoongeServerStoppingEvent extends SpoongeStateEvent implements ServerStoppingEvent
{

    public SpoongeServerStoppingEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }
}
