package org.craft.utils;

import java.util.*;

public class ObjectPool<T> implements Collection<T>
{

    private Stack<T>         stack;
    private ObjectFactory<T> fact;
    private Class<T>         typeClass;

    public ObjectPool(Class<T> clazz)
    {
        this(clazz, new DefaultObjectFactory<T>());
    }

    public ObjectPool(Class<T> clazz, ObjectFactory<T> fact)
    {
        this.typeClass = clazz;
        this.fact = fact;
        stack = new Stack<T>();
    }

    public T get()
    {
        T instance = null;
        if(stack.isEmpty() || stack.size() == 0)
        {
            instance = fact.createNew(typeClass);
        }
        else
        {
            try
            {
                instance = stack.pop();
            }
            catch(Exception e)
            {
                // Silently ignore this
                instance = fact.createNew(typeClass);
            }
        }
        return instance;
    }

    public void dispose(T item)
    {
        stack.push(item);
    }

    @Override
    public int size()
    {
        return stack.size();
    }

    @Override
    public boolean isEmpty()
    {
        return stack.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return stack.contains(o);
    }

    @Override
    public Iterator<T> iterator()
    {
        return stack.iterator();
    }

    @Override
    public Object[] toArray()
    {
        return stack.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return stack.toArray(a);
    }

    @Override
    public boolean add(T e)
    {
        return stack.add(e);
    }

    @Override
    public boolean remove(Object o)
    {
        return stack.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return stack.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        return stack.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        return stack.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        return stack.retainAll(c);
    }

    @Override
    public void clear()
    {
        stack.clear();
    }

}
