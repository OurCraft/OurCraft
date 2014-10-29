package org.craft.modding.events;

import org.craft.*;

public class ModInitEvent extends ModEvent
{

    public ModInitEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
