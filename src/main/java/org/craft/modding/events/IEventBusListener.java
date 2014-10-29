package org.craft.modding.events;

import java.lang.annotation.*;

public interface IEventBusListener
{
    void onEvent(Object event, Object instance, Class<? extends Annotation> annotClass);
}
