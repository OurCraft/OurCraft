package org.craft.modding.events.state;

import org.craft.*;
import org.craft.modding.events.*;

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
