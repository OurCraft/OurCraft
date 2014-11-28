package org.craft.spongeimpl.events;

import org.craft.*;
import org.craft.modding.events.*;
import org.spongepowered.api.util.event.*;

public abstract class SpongeEvent extends ModEvent implements Event, Cancellable
{

    public SpongeEvent(OurCraftInstance instance)
    {
        super(instance);
    }

    private boolean cancelled;

    public boolean isCancelled()
    {
        return cancelled;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }

}
