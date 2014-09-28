package org.craft.spongeimpl.events.player;

import org.craft.spongeimpl.events.*;
import org.spongepowered.api.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.event.player.*;

public abstract class SpongePlayerEvent extends SpongeGameEvent implements PlayerEvent
{

    private Player player;

    public SpongePlayerEvent(Game game, Player player)
    {
        super(game);
        this.player = player;
    }

    @Override
    public Player getPlayer()
    {
        return player;
    }

}
