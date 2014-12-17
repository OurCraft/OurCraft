package org.craft.modding.events.entity;

import org.craft.*;
import org.craft.entity.*;

public class ModEntityDeathEvent extends ModEntityEvent
{

    public ModEntityDeathEvent(OurCraftInstance instance, Entity e)
    {
        super(instance, e);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
