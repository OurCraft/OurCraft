package org.craft.modding.events;

import java.lang.annotation.*;

public class EventListener implements IEventListener
{
    private Object                      listener;
    private String                      methodName;
    private boolean                     enabled = true;
    private Class<?>                    eventClass;
    private Class<? extends Annotation> annotClass;

    public EventListener(Object object, String methodName, Class<?> eventClass, Class<? extends Annotation> annotClass)
    {
        this.eventClass = eventClass;
        this.listener = object;
        this.methodName = methodName;
        this.annotClass = annotClass;
    }

    public Class<? extends Annotation> getAnnotClass()
    {
        return annotClass;
    }

    public Object getListener()
    {
        return listener;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void enable()
    {
        this.enabled = true;
    }

    public void disable()
    {
        this.enabled = false;
    }

    public Class<?> getEventClass()
    {
        return eventClass;
    }

    public void invoke(Object e)
    {
        ;
    }
}
