package org.craft.modding.script.lua;

import java.lang.annotation.*;
import java.util.*;

import org.craft.modding.*;
import org.craft.modding.events.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

import com.google.common.collect.Lists;

public class LuaEventBusListener implements IEventBusListener
{

    private List<LuaEventListener> listeners;

    public LuaEventBusListener()
    {
        listeners = Lists.newArrayList();
    }

    @Override
    public void onEvent(Object event, Object instance, Class<? extends Annotation> annotClass)
    {
        if(annotClass == Mod.class || annotClass == null)
        {
            for(LuaEventListener listener : listeners)
            {
                if(instance == listener.getContainer() || instance == null)
                {
                    String eventName = listener.getEventName();
                    LuaFunction handler = listener.getHandler();
                    if(event.getClass().getSimpleName().replaceFirst("Sponge", "").equals(eventName))
                    {
                        try
                        {
                            handler.invoke(CoerceJavaToLua.coerce(event));
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void register(LuaEventListener luaEventListener)
    {
        listeners.add(luaEventListener);
    }

}
