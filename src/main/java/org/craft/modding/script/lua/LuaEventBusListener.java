package org.craft.modding.script.lua;

import java.lang.annotation.*;
import java.util.*;

import org.craft.modding.*;
import org.craft.modding.events.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;
import org.spongepowered.api.event.*;

public class LuaEventBusListener implements IEventBusListener
{

    private ArrayList<LuaEventListener> listeners;

    public LuaEventBusListener()
    {
        listeners = new ArrayList<LuaEventListener>();
    }

    @Override
    public void onEvent(Event event, Object instance, Class<? extends Annotation> annotClass)
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
                            handler.call(CoerceJavaToLua.coerce(event));
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
