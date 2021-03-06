package org.craft.utils;

import java.lang.reflect.*;

public class DefaultObjectFactory<T> implements ObjectFactory<T>
{

    @Override
    public T createNew(Class<T> typeClass)
    {
        try
        {
            /*System.out.println(typeClass.getName());
            Constructor<T> cons = typeClass.getDeclaredConstructor(new Class<?>[0]);
            cons.setAccessible(true);
            return cons.newInstance(new Object[0]);*/
            return typeClass.getDeclaredConstructor().newInstance();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
