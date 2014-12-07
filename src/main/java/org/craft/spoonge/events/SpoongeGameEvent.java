package org.craft.spoonge.events;

import org.craft.*;
import org.craft.spoonge.*;
import org.spongepowered.api.*;
import org.spongepowered.api.event.*;

public abstract class SpoongeGameEvent extends SpoongeEvent implements GameEvent
{

    public SpoongeGameEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    @Override
    public Game getGame()
    {
        return SpoongeMod.instance;
    }

}
