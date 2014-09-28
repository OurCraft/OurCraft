package org.craft.spongeimpl.events;

import org.spongepowered.api.event.*;

public abstract class SpongeEvent implements Event, Cancellable
{

    private Result  result;
    private boolean cancelled;

    @Override
    public Result getResult()
    {
        return result;
    }

    @Override
    public void setResult(Result result)
    {
        this.result = result;
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
