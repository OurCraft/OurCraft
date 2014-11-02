package org.craft.spongeimpl;

import org.craft.modding.events.*;
import org.spongepowered.api.event.*;
import org.spongepowered.api.plugin.*;

public class SpoongeEventManager implements EventManager
{

    private EventBus eventBus;

    public SpoongeEventManager(EventBus eventBus)
    {
        this.eventBus = eventBus;
    }

    @Override
    public void register(Object obj)
    {
        eventBus.register(obj);
    }

    @Override
    public void unregister(Object obj)
    {
        eventBus.unregister(obj);
    }

    @Override
    public boolean call(Event event)
    {
        return eventBus.fireEvent(event, null, Plugin.class);
    }

}
