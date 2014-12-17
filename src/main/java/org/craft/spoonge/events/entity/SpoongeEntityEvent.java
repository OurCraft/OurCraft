package org.craft.spoonge.events.entity;

import org.craft.*;
import org.craft.spoonge.events.*;
import org.spongepowered.api.entity.*;

public abstract class SpoongeEntityEvent extends SpoongeGameEvent
{

    private Entity entity;

    public SpoongeEntityEvent(OurCraftInstance instance, Entity e)
    {
        super(instance);
        this.entity = e;
    }

    public Entity getEntity()
    {
        return entity;
    }
}
