package org.craft.modding.events.state;

import org.craft.*;
import org.craft.modding.events.*;

public class ModServerStartingEvent extends ModEvent
{

    public ModServerStartingEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
