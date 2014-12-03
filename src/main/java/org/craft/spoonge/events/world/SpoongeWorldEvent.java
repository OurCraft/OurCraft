package org.craft.spoonge.events.world;

import org.craft.*;
import org.craft.spoonge.events.*;
import org.spongepowered.api.event.world.*;
import org.spongepowered.api.world.*;

public abstract class SpoongeWorldEvent extends SpoongeGameEvent implements WorldEvent
{

    private World world;

    public SpoongeWorldEvent(OurCraftInstance game, World world)
    {
        super(game);
        this.world = world;
    }

    @Override
    public World getWorld()
    {
        return world;
    }

}
