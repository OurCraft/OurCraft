package org.craft.modding.script.lua;

import org.craft.*;
import org.craft.utils.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

public class OurCraftLib extends TwoArgFunction
{

    private LuaEventBusListener eventBus;
    private LuaAddonContainer   container;
    private OurCraftInstance    game;

    public OurCraftLib(LuaEventBusListener eventBus, LuaAddonContainer container, OurCraftInstance game)
    {
        this.game = game;
        this.eventBus = eventBus;
        this.container = container;
    }

    public class GetGameRegistryFunc extends LuaFunction
    {
        @Override
        public LuaValue call(LuaValue name, LuaValue method)
        {
            return CoerceJavaToLua.coerce(game.getRegistry());
        }
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
        table.set("getGameRegistry", new GetGameRegistryFunc());
        par2.set("OurCraftAPI", table);
        par2.get("package").get("loaded").set("OurCraftAPI", table);
        return NIL;
    }

}
