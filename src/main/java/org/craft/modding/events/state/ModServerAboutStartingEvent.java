package org.craft.modding.events.state;

import org.craft.*;
import org.craft.modding.events.*;

public class ModServerAboutStartingEvent extends ModEvent
{

    public ModServerAboutStartingEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    @Override
    public boolean isCancellable()
    {
        return false;
    }

}
