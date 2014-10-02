package org.craft.modding.script;

public interface ScriptValue
{

    public static final ScriptValue NULL         = new NullScriptValue();
    public static final String      TYPE_STRING  = "TString";
    public static final String      TYPE_INTEGER = "TInt";
    public static final String      TYPE_DOUBLE  = "TDouble";
    public static final String      TYPE_BOOLEAN = "TBoolean";
    public static final String      TYPE_UNKNOWN = "TUnknown";
    public static final String      TYPE_TABLE   = "TTable";

    public ScriptValue getComponent(int index);

    public ScriptValue getComponent(String name);

    public int length();

    public String toString();

    public int asInt();

    public float asFloat();

    public double asDouble();

    public boolean asBoolean();

    public ScriptValue invoke(ScriptValue... values);

    public abstract String getType();
}
