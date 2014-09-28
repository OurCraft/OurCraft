package org.craft.spongeimpl.events;

import java.lang.reflect.*;
import java.util.*;

import org.craft.utils.*;
import org.spongepowered.api.event.*;

public class EventBus implements EventManager
{

    private HashMap<Class<? extends Event>, ArrayList<EventListener>> listeners;

    public EventBus()
    {
        listeners = new HashMap<Class<? extends Event>, ArrayList<EventListener>>();
    }

    public HashMap<Class<? extends Event>, ArrayList<EventListener>> getListeners()
    {
        return listeners;
    }

    public void register(Object object)
    {
        if(!hasListener(object))
        {
            List<Method> methods = asList(object.getClass().getDeclaredMethods());
            Class<?> clazz = object.getClass().getSuperclass();
            while(clazz != null)
            {
                List<Method> superclassMethods = Arrays.asList(clazz.getDeclaredMethods());
                for(Method method : superclassMethods)
                {
                    methods.add(method);
                }
                clazz = clazz.getSuperclass();
            }
            for(Method method : methods)
            {
                if(method.isAnnotationPresent(SpongeEventHandler.class))
                {
                    if(method.getParameterTypes().length > 1)
                    {
                        Log.error("Method " + method.getName() + " is declared as event listener but has more than one parameter");
                        return;
                    }
                    else if(method.getParameterTypes() == null || method.getParameterTypes().length == 0)
                    {
                        Log.error("Method " + method.getName() + " is declared as event listener but has no parameter");
                        return;
                    }
                    if(Event.class.isAssignableFrom(method.getParameterTypes()[0]))
                    {
                        @SuppressWarnings("unchecked")
                        Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];
                        EventListener listener = new EventListener(object, method.getName(), eventClass);
                        ArrayList<EventListener> list = listeners.get(eventClass);
                        if(list == null)
                            list = new ArrayList<EventListener>();
                        list.add(listener);
                        listeners.put(eventClass, list);
                    }
                    else
                    {
                        Log.error("Method " + method.getName() + " is declared as event listener but the parameter of type " + method.getParameterTypes()[0].getName() + " can't be cast to an Event instance");
                    }
                }
            }
        }
    }

    private ArrayList<Method> asList(Method[] declaredMethods)
    {
        ArrayList<Method> methods = new ArrayList<Method>();
        for(Method declared : declaredMethods)
            methods.add(declared);
        return methods;
    }

    public boolean hasListener(Object object)
    {
        Iterator<ArrayList<EventListener>> it = getListeners().values().iterator();
        while(it.hasNext())
        {
            ArrayList<EventListener> list = it.next();
            if(list.contains(object))
                return true;
        }
        return false;
    }

    @Override
    public void unregister(Object obj)
    {
        listeners.remove(obj.getClass());
    }

    @Override
    public boolean call(Event event)
    {
        return fireEvent(event, null);
    }

    public boolean fireEvent(Event e, Object instance)
    {
        HashMap<Class<? extends Event>, ArrayList<EventListener>> listenersMap = getListeners();
        for(ArrayList<EventListener> list : listenersMap.values())
        {
            if(!list.isEmpty())
            {
                for(EventListener listener : list)
                {
                    if(listener.isEnabled() && (instance == null || listener.getListener() == instance) && listener.getEventClass().isAssignableFrom(e.getClass()))
                        try
                        {
                            Method m = listener.getListener().getClass().getDeclaredMethod(listener.getMethodName(), listener.getEventClass());
                            m.setAccessible(true);
                            m.invoke(listener.getListener(), e);
                            listener.disable();
                        }
                        catch(Exception e1)
                        {
                            e1.printStackTrace();
                        }
                }
                for(EventListener listener : list)
                {
                    listener.enable();
                }
            }
        }
        boolean cancelled = false;
        if(e instanceof Cancellable)
        {
            cancelled = ((Cancellable) e).isCancelled();
        }
        return cancelled && e.isCancellable();
    }
}
