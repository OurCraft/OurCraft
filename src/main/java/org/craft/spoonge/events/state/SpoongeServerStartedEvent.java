package org.craft.spoonge.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpoongeServerStartedEvent extends SpoongeStateEvent implements ServerStartedEvent
{

    public SpoongeServerStartedEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
