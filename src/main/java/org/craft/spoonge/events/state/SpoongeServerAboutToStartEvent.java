package org.craft.spoonge.events.state;

import org.craft.*;
import org.spongepowered.api.event.state.*;

public class SpoongeServerAboutToStartEvent extends SpoongeStateEvent implements ServerAboutToStartEvent
{

    public SpoongeServerAboutToStartEvent(OurCraftInstance game)
    {
        super(game);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
