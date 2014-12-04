package org.craft.spoonge.events;

import org.craft.*;
import org.craft.modding.events.*;
import org.spongepowered.api.util.event.*;
import org.spongepowered.api.util.event.callback.*;

public abstract class SpoongeEvent extends ModEvent implements Event, Cancellable
{

    private CallbackList callbacksList;

    public SpoongeEvent(OurCraftInstance instance)
    {
        super(instance);
        callbacksList = new CallbackList();
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

    public CallbackList getCallbacks()
    {
        return callbacksList;
    }

}
