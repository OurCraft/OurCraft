package org.craft.modding.script.lua;

import org.craft.modding.script.*;
import org.luaj.vm2.*;

public class LuaScriptVarargs implements ScriptValue
{

    private Varargs value;

    public LuaScriptVarargs(Varargs val)
    {
        this.value = val;
    }

    @Override
    public ScriptValue getComponent(int index)
    {
        return new LuaScriptValue(value.arg(index));
    }

    @Override
    public ScriptValue getComponent(String name)
    {
        return new LuaScriptValue(value.arg1().get(name));
    }

    @Override
    public int length()
    {
        return value.narg();
    }

    @Override
    public String toString()
    {
        return value.toString();
    }

    @Override
    public int asInt()
    {
        return 0;
    }

    @Override
    public float asFloat()
    {
        return 0;
    }

    @Override
    public double asDouble()
    {
        return 0;
    }

    @Override
    public boolean asBoolean()
    {
        return true;
    }

    @Override
    public ScriptValue invoke(ScriptValue... values)
    {
        return this;
    }

    @Override
    public String getType()
    {
        return ScriptValue.TYPE_TABLE;
    }

}
