package org.craft.modding.script.lua;

import java.io.*;

import org.craft.*;
import org.craft.resources.*;
import org.luaj.vm2.*;

public class RequireFunction extends LuaFunction
{

    private AbstractResource    scriptResource;
    private Globals             globals;
    private LuaEventBusListener eventBus;
    private LuaAddonContainer   container;
    private OurCraftInstance    game;

    public RequireFunction(AbstractResource scriptResource, Globals globals, LuaEventBusListener eventBus, LuaAddonContainer container, OurCraftInstance game)
    {
        this.game = game;
        this.container = container;
        this.scriptResource = scriptResource;
        this.eventBus = eventBus;
        this.globals = globals;
    }

    @Override
    @SuppressWarnings("unused")
    public LuaValue call(LuaValue arg)
    {
        String scriptName = arg.toString();
        try
        {
            AbstractResource newRes = scriptResource.getLoader().getResource(scriptResource.getResourceLocation().getDirectParent().getChild(scriptName));
            new LuaScript(newRes, globals, eventBus, container, game);
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return LuaValue.NIL;
    }
}
