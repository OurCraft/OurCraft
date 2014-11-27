package org.craft.utils;

public class ReferencedObjectPool<T extends AbstractReference> extends ObjectPool<T>
{

    public static <K extends AbstractReference> ReferencedObjectPool<K> of(Class<K> clazz)
    {
        return new ReferencedObjectPool<K>(clazz);
    }

    public static <K extends AbstractReference> ReferencedObjectPool<K> of(Class<K> clazz, ObjectFactory<K> fact)
    {
        return new ReferencedObjectPool<K>(clazz, fact);
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
        if(item.decreaseReferenceCounter() && item.isDisposable())
            super.dispose(item);
    }

}
