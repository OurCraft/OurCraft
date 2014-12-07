package org.craft.modding.script.lua.funcs;

import org.craft.modding.script.lua.*;
import org.craft.utils.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class RegisterHandlerFunc extends TwoArgFunction
{
    private LuaEventBusListener eventBus;
    private LuaAddonContainer   container;

    public RegisterHandlerFunc(LuaEventBusListener eventBus, LuaAddonContainer container)
    {
        this.eventBus = eventBus;
        this.container = container;
    }

    @Override
    public LuaValue call(LuaValue name, LuaValue method)
    {
        String eventName = name.toString();
        LuaFunction function = method.checkfunction();
        Log.message("Registred " + eventName + " listener: " + function.tojstring());
        eventBus.register(new LuaEventListener(eventName, function, container));
        return LuaValue.TRUE;
    }
}
