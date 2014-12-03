package org.craft.spoonge;

import org.craft.modding.events.*;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.service.event.*;
import org.spongepowered.api.util.event.*;

public class SpoongeEventManager implements EventManager
{

    private EventBus eventBus;

    public SpoongeEventManager(EventBus eventBus)
    {
        this.eventBus = eventBus;
    }

    @Override
    public void unregister(Object obj)
    {
        eventBus.unregister(obj);
    }

    @Override
    public void register(Object plugin, Object obj)
    {
        eventBus.register(obj);
    }

    @Override
    public boolean post(Event event)
    {
        return eventBus.fireEvent(event, null, Plugin.class);
    }

}
