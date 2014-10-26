package org.craft.utils;

import java.lang.reflect.*;

public class DefaultObjectFactory<T> implements ObjectFactory<T>
{

    @Override
    public T createNew(Class<T> typeClass)
    {
        try
        {
            Constructor<T> cons = typeClass.getDeclaredConstructor(new Class<?>[0]);
            cons.setAccessible(true);
            return cons.newInstance(new Object[0]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
