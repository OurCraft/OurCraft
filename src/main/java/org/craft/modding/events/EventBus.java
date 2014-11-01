package org.craft.modding.events;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.utils.*;
import org.spongepowered.api.event.*;

public class EventBus
{

    private HashMap<Class<?>, ArrayList<EventListener>> listeners;
    private ArrayList<Class<? extends Annotation>>      annotations;
    private ArrayList<IEventBusListener>                eventBusListeners;
    private ArrayList<Class<?>>                         eventClasses;

    public EventBus(Class<?>[] eventClasses, Class<? extends Annotation>... annots)
    {
        this.eventClasses = Lists.newArrayList(eventClasses);
        this.eventBusListeners = new ArrayList<IEventBusListener>();
        this.annotations = Lists.newArrayList(annots);
        listeners = new HashMap<Class<?>, ArrayList<EventListener>>();
    }

    public void addAnnotationClass(Class<? extends Annotation> annotClass)
    {
        annotations.add(annotClass);
    }

    public void addEventClass(Class<?> eventClass)
    {
        eventClasses.add(eventClass);
    }

    public HashMap<Class<?>, ArrayList<EventListener>> getListeners()
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
                for(Class<? extends Annotation> annotation : annotations)
                {
                    if(methodHasAnnot(annotation, method))
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
                        for(Class<?> eventTypeClass : eventClasses)
                        {
                            Class<?> eventClass = (Class<?>) method.getParameterTypes()[0];
                            EventListener listener = new EventListener(object, method.getName(), eventClass, annotation);
                            ArrayList<EventListener> list = listeners.get(eventClass);
                            if(list == null)
                                list = new ArrayList<EventListener>();
                            list.add(listener);
                            listeners.put(eventClass, list);
                        }
                    }
                    else
                    {
                        Log.error("No annotation for you! " + method.getName());
                    }
                }
            }
        }
    }

    private boolean methodHasAnnot(Class<? extends Annotation> annotation, Method method)
    {
        for(Annotation annot : method.getAnnotations())
        {
            if(annot.annotationType().getCanonicalName().equals(annotation.getCanonicalName()))
                return true;
        }
        return false;
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

    public void unregister(Object obj)
    {
        listeners.remove(obj.getClass());
    }

    public boolean fireEvent(Object e, Object instance, Class<? extends Annotation> annotClass)
    {
        for(IEventBusListener listener : eventBusListeners)
            listener.onEvent(e, instance, annotClass);
        try
        {
            for(ArrayList<EventListener> list : listeners.values())
            {
                if(!list.isEmpty())
                {
                    for(int i = 0; i < list.size(); i++ )
                    {
                        EventListener listener = list.get(i);
                        if(listener.isEnabled() && (instance == null || listener.getListener() == instance))
                        {
                            if(listener.getEventClass().isAssignableFrom(e.getClass()))
                            {
                                if(listener.getAnnotClass() == annotClass || annotClass == null)
                                {
                                    try
                                    {
                                        Method m = listener.getListener().getClass().getMethod(listener.getMethodName(), new Class<?>[]
                                        {
                                                listener.getEventClass()
                                        });
                                        m.setAccessible(true);
                                        m.invoke(listener.getListener(), new Object[]
                                        {
                                                e
                                        });
                                        listener.disable();
                                    }
                                    catch(Exception e1)
                                    {
                                        e1.printStackTrace();
                                    }
                                }
                                else
                                {
                                    Log.error(listener.getAnnotClass() + " != " + annotClass);
                                }
                            }
                            else
                            {
                                Log.error(listener.getEventClass() + " not assignable from " + e.getClass());
                            }
                        }
                    }
                    for(EventListener listener : list)
                    {
                        listener.enable();
                    }
                }
            }
        }
        catch(ConcurrentModificationException ex)
        {
            ; // Shhhh, be quiet
              // Yes, this way of handling exceptions is bad, but do a better implementation of EventBus if you think you can't do better
        }
        boolean cancelled = false;
        if(e instanceof Cancellable)
        {
            cancelled = ((Cancellable) e).isCancelled();
        }
        boolean cancellable = false;
        if(e instanceof Event)
        {
            cancellable = ((Event) e).isCancellable();
        }
        if(e instanceof ModEvent)
        {
            cancellable = ((ModEvent) e).isCancellable();
        }
        return cancelled && cancellable;
    }

    public void addListener(IEventBusListener listener)
    {
        eventBusListeners.add(listener);
    }

    public void merge(EventBus other)
    {
        listeners.putAll(other.listeners);
    }
}
