package org.craft.modding.script.lua;

import java.io.*;

import org.craft.modding.script.*;
import org.craft.resources.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;
import org.spongepowered.api.*;

public class LuaScript extends Script
{
    private Globals             globals;
    private LuaEventBusListener eventBus;
    private LuaAddonContainer   container;
    private Game                game;

    public LuaScript(AbstractResource scriptRes, LuaEventBusListener eventBus, LuaAddonContainer container, Game game) throws UnsupportedEncodingException
    {
        super();
        this.game = game;
        this.eventBus = eventBus;
        this.container = container;
        init(scriptRes);
    }

    public LuaScript(AbstractResource newRes, Globals globals, LuaEventBusListener eventBus, LuaAddonContainer container, Game game) throws UnsupportedEncodingException
    {
        super();
        this.game = game;
        this.container = container;
        this.eventBus = eventBus;
        this.globals = globals;
        init(newRes);
    }

    @Override
    protected void compile()
    {
        if(globals == null)
            globals = JsePlatform.debugGlobals();
        globals.set("require", new RequireFunction(scriptResource, globals, eventBus, container, game));
        globals.set("print", new PrintFunction(globals));
        globals.load(new OurCraftLib(eventBus, container, game));
        globals.load(scriptSource, scriptResource.getResourceLocation().getName()).call();
    }

    @Override
    public ScriptValue run()
    {
        return new LuaScriptValue(globals.load(scriptSource).call());
    }

    @Override
    public ScriptValue run(String method, Object... args)
    {
        LuaValue[] luaArgs = new LuaValue[args.length];
        for(int i = 0; i < args.length; i++ )
        {
            Object arg = args[i];
            if(arg instanceof String)
            {
                luaArgs[i] = LuaValue.valueOf((String) arg);
            }
            else if(arg instanceof Integer)
            {
                luaArgs[i] = LuaValue.valueOf((Integer) arg);
            }
            else if(arg instanceof Double)
            {
                luaArgs[i] = LuaValue.valueOf((Double) arg);
            }
            else if(arg instanceof Boolean)
            {
                luaArgs[i] = LuaValue.valueOf((Boolean) arg);
            }
            else if(arg instanceof byte[])
            {
                luaArgs[i] = LuaValue.valueOf((byte[]) arg);
            }
            else
                luaArgs[i] = LuaValue.NIL;
        }
        return new LuaScriptVarargs(globals.get(method).invoke(LuaValue.varargsOf(luaArgs)));
    }

}
