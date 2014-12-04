package org.craft.spoonge.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpoongePostInitEvent extends SpoongeStateEvent implements PostInitializationEvent
{

    public SpoongePostInitEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
