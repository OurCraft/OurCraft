package org.craft.modding.events.state;

import org.craft.*;
import org.craft.modding.events.*;

public class ModServerStoppedEvent extends ModEvent
{

    public ModServerStoppedEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
