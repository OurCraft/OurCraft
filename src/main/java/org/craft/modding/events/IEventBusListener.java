package org.craft.modding.events;

import java.lang.annotation.*;

import org.spongepowered.api.event.*;

public interface IEventBusListener
{
    void onEvent(Event event, Object instance, Class<? extends Annotation> annotClass);
}
