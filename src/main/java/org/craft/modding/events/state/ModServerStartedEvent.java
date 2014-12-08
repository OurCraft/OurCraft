package org.craft.modding.events.state;

import org.craft.*;
import org.craft.modding.events.*;

public class ModServerStartedEvent extends ModEvent
{

    public ModServerStartedEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
