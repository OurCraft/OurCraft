package org.craft.spoonge.events.player;

import org.craft.*;
import org.craft.spoonge.events.*;
import org.spongepowered.api.entity.player.*;
import org.spongepowered.api.event.player.*;

public abstract class SpoongePlayerEvent extends SpoongeGameEvent implements PlayerEvent
{

    private Player player;

    public SpoongePlayerEvent(OurCraftInstance game, Player player)
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
