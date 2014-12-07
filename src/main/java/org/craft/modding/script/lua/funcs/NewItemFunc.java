package org.craft.modding.script.lua.funcs;

import org.craft.items.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

public class NewItemFunc extends OneArgFunction
{

    @Override
    public LuaValue call(LuaValue arg)
    {
        Item item = new Item(arg.toString());
        return CoerceJavaToLua.coerce(item);
    }

}
