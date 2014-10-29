package org.craft.spongeimpl.events;

import org.craft.*;
import org.craft.spongeimpl.*;
import org.spongepowered.api.*;
import org.spongepowered.api.event.*;

public abstract class SpongeGameEvent extends SpongeEvent implements GameEvent
{

    public SpongeGameEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    public Game getGame()
    {
        return SpoongeMod.instance;
    }

}
