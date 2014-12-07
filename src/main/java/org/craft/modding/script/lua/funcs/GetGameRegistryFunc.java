package org.craft.modding.script.lua.funcs;

import org.craft.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

public class GetGameRegistryFunc extends LuaFunction
{
    private OurCraftInstance game;

    public GetGameRegistryFunc(OurCraftInstance game)
    {
        this.game = game;
    }

    @Override
    public LuaValue call(LuaValue name, LuaValue method)
    {
        return CoerceJavaToLua.coerce(game.getRegistry());
    }
}
