package org.craft.modding.script.lua;

import org.luaj.vm2.*;

public class LuaEventListener
{

    private String            eventName;
    private LuaFunction       handler;
    private LuaAddonContainer container;

    public LuaEventListener(String eventName, LuaFunction handler, LuaAddonContainer container)
    {
        this.eventName = eventName;
        this.handler = handler;
        this.container = container;
    }

    public String getEventName()
    {
        return eventName;
    }

    public LuaFunction getHandler()
    {
        return handler;
    }

    public Object getContainer()
    {
        return container;
    }
}
