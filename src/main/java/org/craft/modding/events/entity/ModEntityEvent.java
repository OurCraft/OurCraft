package org.craft.modding.events.entity;

import org.craft.*;
import org.craft.entity.*;
import org.craft.modding.events.*;

public abstract class ModEntityEvent extends ModEvent
{

    private Entity entity;

    public ModEntityEvent(OurCraftInstance instance, Entity e)
    {
        super(instance);
        this.entity = e;
    }

    public Entity getEntity()
    {
        return entity;
    }

}
