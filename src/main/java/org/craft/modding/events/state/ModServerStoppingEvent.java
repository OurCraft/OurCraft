package org.craft.modding.events.state;

import org.craft.*;
import org.craft.modding.events.*;

public class ModServerStoppingEvent extends ModEvent
{

    public ModServerStoppingEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
