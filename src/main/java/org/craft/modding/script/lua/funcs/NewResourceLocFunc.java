package org.craft.modding.script.lua.funcs;

import org.craft.resources.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

public class NewResourceLocFunc extends TwoArgFunction
{
    @Override
    public LuaValue call(LuaValue sectionValue, LuaValue pathValue)
    {
        String section = sectionValue.toString();
        String function = pathValue.toString();
        return CoerceJavaToLua.coerce(new ResourceLocation(section, function));
    }
}
