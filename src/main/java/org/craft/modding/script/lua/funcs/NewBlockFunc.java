package org.craft.modding.script.lua.funcs;

import org.craft.blocks.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

public class NewBlockFunc extends OneArgFunction
{

    @Override
    public LuaValue call(LuaValue arg)
    {
        Block block = new Block(arg.toString());
        return CoerceJavaToLua.coerce(block);
    }

}
