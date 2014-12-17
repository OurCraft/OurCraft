package org.craft.spoonge.events.entity;

import org.craft.*;
import org.spongepowered.api.entity.*;

public class SpoongeEntityDeathEvent extends SpoongeEntityEvent
{

    public SpoongeEntityDeathEvent(OurCraftInstance instance, Entity entity)
    {
        super(instance, entity);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
