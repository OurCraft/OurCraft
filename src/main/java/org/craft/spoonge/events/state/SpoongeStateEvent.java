package org.craft.spoonge.events.state;

import org.craft.*;
import org.craft.spoonge.events.*;
import org.spongepowered.api.event.state.*;

public abstract class SpoongeStateEvent extends SpoongeGameEvent implements StateEvent
{

    public SpoongeStateEvent(OurCraftInstance game)
    {
        super(game);
    }

}
