package org.craft.modding.script.lua.funcs;

import java.io.*;

import org.craft.modding.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

public class NewConfigurationFunc extends OneArgFunction
{

    @Override
    public LuaValue call(LuaValue arg)
    {
        File file = (File) arg.touserdata();
        return CoerceJavaToLua.coerce(new Configuration(file));
    }

}
