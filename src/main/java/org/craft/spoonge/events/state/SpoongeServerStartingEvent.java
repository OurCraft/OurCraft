package org.craft.spoonge.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpoongeServerStartingEvent extends SpoongeStateEvent implements ServerStartingEvent
{

    public SpoongeServerStartingEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
