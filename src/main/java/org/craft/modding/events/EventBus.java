package org.craft.modding.events;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;
import com.google.common.reflect.*;

import org.craft.utils.*;
import org.spongepowered.api.event.*;

public class EventBus
{

    private HashMap<Class<?>, ArrayList<IEventListener>> listeners;
    private ArrayList<Class<? extends Annotation>>       annotations;
    private ArrayList<IEventBusListener>                 eventBusListeners;
    private ArrayList<Class<?>>                          eventClasses;

    public EventBus(Class<?>[] eventClasses, Class<? extends Annotation>... annots)
    {
        this.eventClasses = Lists.newArrayList(eventClasses);
        this.eventBusListeners = new ArrayList<IEventBusListener>();
        this.annotations = Lists.newArrayList(annots);
        listeners = new HashMap<Class<?>, ArrayList<IEventListener>>();
    }

    public void addAnnotationClass(Class<? extends Annotation> annotClass)
    {
        annotations.add(annotClass);
    }

    public void addEventClass(Class<?> eventClass)
    {
        eventClasses.add(eventClass);
    }

    public HashMap<Class<?>, ArrayList<IEventListener>> getListeners()
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
            ArrayList<String> checked = new ArrayList<String>();
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
                                        Log.error("Method " + method.getName() + " is declared as event listener but has more than one parameter");
                                        return;
                                    }
                                    else if(realMethod.getParameterTypes() == null || method.getParameterTypes().length == 0)
                                    {
                                        Log.error("Method " + method.getName() + " is declared as event listener but has no parameter");
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
                            ;// e1.printStackTrace();
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
            ArrayList<IEventListener> list = listeners.get(eventClass);
            if(list == null)
                list = new ArrayList<IEventListener>();
            list.add(listener);
            Log.message("Registred " + m.getDeclaringClass().getName() + ":" + m.getName() + " >> " + eventClass);
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
        Iterator<ArrayList<IEventListener>> it = getListeners().values().iterator();
        while(it.hasNext())
        {
            ArrayList<IEventListener> list = it.next();
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
        for(IEventBusListener listener : eventBusListeners)
            listener.onEvent(e, instance, annotClass);
        ArrayList<IEventListener>[] values = listeners.values().toArray(new ArrayList[0]);
        for(ArrayList<IEventListener> list : values)
        {
            for(IEventListener listener : list)
            {
                listener.invoke(e);
            }
        }
        /*     try
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
                                 if(TypeToken.of(listener.getEventClass()).getType().getTypeName().equals(TypeToken.of(e.getClass()).getType().getTypeName()))
                                 {
                                     if(listener.getAnnotClass() == annotClass || annotClass == null)
                                     {
                                         try
                                         {
                                             Set<? extends Class<?>> classes = TypeToken.of(listener.getListener().getClass()).getTypes().rawTypes();
                                             Set<? extends Class<?>> eventSubclasses = TypeToken.of(listener.getEventClass()).getTypes().rawTypes();
                                             for(Class<?> cls : classes)
                                             {
                                                 for(Class<?> evtCls : eventSubclasses)
                                                 {
                                                     try
                                                     {
                                                         Method m = cls.getMethod(listener.getMethodName(), new Class<?>[]
                                                         {
                                                                 evtCls
                                                         });
                                                         m.setAccessible(true);
                                                         m.invoke(listener.getListener(), new Object[]
                                                         {
                                                                 e
                                                         });
                                                         listener.disable();
                                                     }
                                                     catch(Exception ex)
                                                     {
                                                         ;
                                                     }
                                                 }
                                             }
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
             }*/
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
