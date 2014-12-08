package org.craft.modding.events.state;

import org.craft.*;
import org.craft.modding.events.*;

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
