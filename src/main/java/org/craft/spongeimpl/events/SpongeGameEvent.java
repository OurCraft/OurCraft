package org.craft.spongeimpl.events;

import org.spongepowered.api.*;
import org.spongepowered.api.event.*;

public abstract class SpongeGameEvent extends SpongeEvent implements GameEvent
{

    private Game game;

    public SpongeGameEvent(Game game)
    {
        this.game = game;
    }

    @Override
    public Game getGame()
    {
        return game;
    }

}
