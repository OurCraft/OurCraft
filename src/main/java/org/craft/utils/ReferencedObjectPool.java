package org.craft.utils;

public class ReferencedObjectPool<T extends AbstractReference> extends ObjectPool<T>
{

    public static <T extends AbstractReference> ReferencedObjectPool<T> of(Class<T> clazz)
    {
        return new ReferencedObjectPool<T>(clazz);
    }

    public static <T extends AbstractReference> ReferencedObjectPool<T> of(Class<T> clazz, ObjectFactory<T> fact)
    {
        return new ReferencedObjectPool<T>(clazz, fact);
    }

    public ReferencedObjectPool(Class<T> clazz)
    {
        this(clazz, new DefaultObjectFactory<T>());
    }

    public ReferencedObjectPool(Class<T> clazz, ObjectFactory<T> fact)
    {
        super(clazz, fact);
    }

    public T get()
    {
        T instance = super.get();
        instance.increaseReferenceCounter();
        return instance;
    }

    public void dispose(T item)
    {
        if(item.decreaseReferenceCounter())
            super.dispose(item);
    }

}
