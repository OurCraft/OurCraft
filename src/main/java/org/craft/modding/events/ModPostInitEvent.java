package org.craft.modding.events;

import org.craft.*;

public class ModPostInitEvent extends ModEvent
{

    public ModPostInitEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
