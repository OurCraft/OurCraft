package org.craft.utils;

public interface ObjectFactory<T>
{
    T createNew(Class<T> typeClass);
}
