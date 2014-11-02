package org.craft.modding.events;

public final class ListenerModel implements IEventListener
{
    private Object instance;

    public ListenerModel(Object instance)
    {
        this.instance = instance;
    }

    public void invoke(Object e)
    {
        try
        {
            instance.getClass().getDeclaredMethod("invoke", e.getClass()).invoke(instance, e);
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }
    }
}
