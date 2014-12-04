package org.craft.spoonge.events.world;

import org.craft.*;
import org.spongepowered.api.event.world.*;
import org.spongepowered.api.world.*;

public class SpoongeWorldUnloadEvent extends SpoongeWorldEvent implements WorldUnloadEvent
{

    public SpoongeWorldUnloadEvent(OurCraftInstance game, World world)
    {
        super(game, world);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
