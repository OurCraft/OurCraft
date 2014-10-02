package org.craft.modding.script;

public class NullScriptValue implements ScriptValue
{

    @Override
    public ScriptValue getComponent(int index)
    {
        throw new NullPointerException("Attempted to access index " + index + " of a null value");
    }

    @Override
    public ScriptValue getComponent(String name)
    {
        throw new NullPointerException("Attempted to access component " + name + " of a null value");
    }

    @Override
    public int length()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return "null";
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
        return false;
    }

    @Override
    public ScriptValue invoke(ScriptValue... values)
    {
        return null;
    }

    @Override
    public String getType()
    {
        return null;
    }

}
