package org.craft.modding.script.lua;

import org.craft.utils.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class OurCraftLib extends TwoArgFunction
{

    private LuaEventBusListener eventBus;
    private LuaAddonContainer   container;

    public OurCraftLib(LuaEventBusListener eventBus, LuaAddonContainer container)
    {
        this.eventBus = eventBus;
        this.container = container;
    }

    public class RegisterHandlerFunc extends TwoArgFunction
    {
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

    @Override
    public LuaValue call(LuaValue par1, LuaValue par2)
    {
        LuaTable table = new LuaTable();
        table.set("registerHandler", new RegisterHandlerFunc());
        par2.set("OurCraftAPI", table);
        par2.get("package").get("loaded").set("OurCraftAPI", table);
        return NIL;
    }

}
