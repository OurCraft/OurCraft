package org.craft.modding.script;

import java.io.*;

import org.craft.resources.*;

public abstract class Script
{

    protected String           scriptSource;
    protected AbstractResource scriptResource;
    private String             filename;

    public Script(AbstractResource scriptRes) throws UnsupportedEncodingException
    {
        init(scriptRes);
    }

    protected void init(AbstractResource scriptRes) throws UnsupportedEncodingException
    {
        scriptSource = new String(scriptRes.getData(), "UTF-8");
        this.scriptResource = scriptRes;
        this.filename = scriptResource.getResourceLocation().getName();
        compile();
    }

    public Script()
    {
    }

    public String getScriptFileName()
    {
        return filename;
    }

    protected abstract void compile();

    public abstract ScriptValue run(String method, Object... args) throws IOException;

    public abstract ScriptValue run();
}
