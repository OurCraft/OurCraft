package org.craft.modding.events;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;
import com.google.common.reflect.*;

import org.craft.utils.*;
import org.spongepowered.api.util.event.*;

public class EventBus
{

    private HashMap<Class<?>, List<IEventListener>> listeners;
    private List<Class<? extends Annotation>>       annotations;
    private List<IEventBusListener>                 eventBusListeners;
    private List<Class<?>>                          eventClasses;

    public EventBus(Class<?>[] eventClasses, List<Class<? extends Annotation>> annots)
    {
        this.eventClasses = Lists.newArrayList(eventClasses);
        this.eventBusListeners = Lists.newArrayList();
        this.annotations = annots;
        listeners = new HashMap<Class<?>, List<IEventListener>>();
    }

    public void addAnnotationClass(Class<? extends Annotation> annotClass)
    {
        annotations.add(annotClass);
    }

    public void addEventClass(Class<?> eventClass)
    {
        eventClasses.add(eventClass);
    }

    public HashMap<Class<?>, List<IEventListener>> getListeners()
    {
        return listeners;
    }

    public void register(Object object)
    {
        if(!hasListener(object))
        {
            List<Method> methods = Lists.newArrayList(object.getClass().getDeclaredMethods());
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
            Set<? extends Class<?>> classes = TypeToken.of(object.getClass()).getTypes().rawTypes();
            List<String> checked = Lists.newArrayList();
            for(Method method : methods)
            {
                for(Class<?> cls : classes)
                {
                    for(Class<? extends Annotation> annotation : annotations)
                    {
                        Method realMethod;
                        try
                        {
                            if(!checked.contains(method.getName()))
                            {
                                realMethod = cls.getDeclaredMethod(method.getName(), method.getParameterTypes());
                                if(methodHasAnnot(annotation, realMethod))
                                {
                                    if(realMethod.getParameterTypes().length > 1)
                                    {
                                        Log.fatal("Method " + method.getName() + " is declared as event listener but has more than one parameter");
                                        return;
                                    }
                                    else if(realMethod.getParameterTypes() == null || method.getParameterTypes().length == 0)
                                    {
                                        Log.fatal("Method " + method.getName() + " is declared as event listener but has no parameter");
                                        return;
                                    }
                                    checked.add(method.getName());
                                    Class<?> eventClass = (Class<?>) method.getParameterTypes()[0];
                                    register(object, realMethod, eventClass);
                                }
                            }
                        }
                        catch(NoSuchMethodException e1)
                        {
                            ;
                        }
                    }
                }
            }
        }
    }

    private void register(Object object, Method m, Class<?> eventClass)
    {
        IEventListener listener;
        try
        {
            listener = new ASMEventListener(object, m);
            List<IEventListener> list = listeners.get(eventClass);
            if(list == null)
                list = Lists.newArrayList();
            list.add(listener);
            listeners.put(eventClass, list);
        }
        catch(Exception e)
        {
            e.printStackTrace();
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

    public boolean hasListener(Object object)
    {
        Iterator<List<IEventListener>> it = getListeners().values().iterator();
        while(it.hasNext())
        {
            List<IEventListener> list = it.next();
            if(list.contains(object))
                return true;
        }
        return false;
    }

    public void unregister(Object obj)
    {
        listeners.remove(obj.getClass());
    }

    @SuppressWarnings("unchecked")
    public boolean fireEvent(Object e, Object instance, Class<? extends Annotation> annotClass)
    {
        for(IEventBusListener eventBusListener : eventBusListeners)
            eventBusListener.onEvent(e, instance, annotClass);
        List<IEventListener>[] values = listeners.values().toArray(new ArrayList[0]);
        for(List<IEventListener> list : values)
        {
            for(IEventListener listener : list)
            {
                listener.invoke(e, instance);
            }
        }
        boolean cancelled = false;
        boolean cancellable = false;
        if(e instanceof Cancellable)
        {
            cancelled = ((Cancellable) e).isCancelled();
            cancellable = true;
        }
        if(e instanceof ModEvent)
        {
            cancellable = ((ModEvent) e).isCancellable();
            cancelled = ((ModEvent) e).isCancelled();
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

    public boolean fireEvent(Object event)
    {
        return fireEvent(event, null, null);
    }
}
