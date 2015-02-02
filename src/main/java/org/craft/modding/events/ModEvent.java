package org.craft.modding.events;

import org.craft.*;

public abstract class ModEvent implements ICancellable
{

    private boolean          cancelled;
    private OurCraftInstance gameInstance;

    public abstract boolean isCancellable();

    public ModEvent(OurCraftInstance instance)
    {
        this.gameInstance = instance;
    }

    public OurCraftInstance getOurCraftInstance()
    {
        return gameInstance;
    }

    public boolean isCancelled()
    {
        return cancelled;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }

}
