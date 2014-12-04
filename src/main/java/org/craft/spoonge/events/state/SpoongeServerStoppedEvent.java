package org.craft.spoonge.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpoongeServerStoppedEvent extends SpoongeStateEvent implements ServerStoppedEvent
{

    public SpoongeServerStoppedEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
