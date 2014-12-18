package org.craft.modding.script.ruby;

import org.craft.modding.script.*;
import org.jruby.*;
import org.jruby.runtime.builtin.*;

public class RubyScriptValue implements ScriptValue
{

    private IRubyObject value;
    private String      type;
    private Ruby        runtime;

    public RubyScriptValue(IRubyObject object)
    {
        this.value = object;
        this.runtime = object.getRuntime();
        this.type = ScriptValue.TYPE_UNKNOWN;
    }

    @Override
    public ScriptValue getComponent(int index)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScriptValue getComponent(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int length()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int asInt()
    {
        return (int) value.convertToInteger().getLongValue();
    }

    @Override
    public float asFloat()
    {
        return (float) value.convertToFloat().getValue();
    }

    @Override
    public double asDouble()
    {
        return value.convertToFloat().getValue();
    }

    @Override
    public boolean asBoolean()
    {
        return false;
    }

    @Override
    public ScriptValue invoke(ScriptValue... values)
    {
        return new RubyScriptValue(value.callMethod(value.getRuntime().getCurrentContext(), "", toObjects(values)));
    }

    private IRubyObject[] toObjects(ScriptValue[] values)
    {
        IRubyObject[] array = new IRubyObject[values.length];
        for(int i = 0; i < array.length; i++ )
        {
            ScriptValue value = values[i];
            if(value.getType().equals(ScriptValue.NULL))
            {
                array[i] = runtime.evalScriptlet("nil"); // TODO: find better way to do that
            }
            else if(value.getType().equals(ScriptValue.TYPE_BOOLEAN))
            {
                array[i] = runtime.newBoolean(value.asBoolean());
            }
            else if(value.getType().equals(ScriptValue.TYPE_DOUBLE))
            {
                array[i] = runtime.newFloat(value.asDouble());
            }
            else if(value.getType().equals(ScriptValue.TYPE_INTEGER))
            {
                array[i] = runtime.newFixnum(value.asInt());
            }
            else if(value.getType().equals(ScriptValue.TYPE_UNKNOWN))
            {
                array[i] = runtime.newString(value.toString());
            }
        }
        return array;
    }

    @Override
    public String getType()
    {
        return type;
    }

}
