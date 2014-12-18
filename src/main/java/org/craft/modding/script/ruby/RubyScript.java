package org.craft.modding.script.ruby;

import java.io.*;

import org.craft.modding.script.*;
import org.craft.resources.*;
import org.jruby.*;

public class RubyScript extends Script
{

    private Ruby   ruby;
    private String name;

    public RubyScript(AbstractResource scriptResource) throws UnsupportedEncodingException
    {
        init(scriptResource);
    }

    @Override
    protected void compile()
    {
        ruby = Ruby.newInstance();
        this.name = scriptResource.getResourceLocation().getName();
        ruby.loadFile(scriptResource.getResourceLocation().getName(), new ByteArrayInputStream(scriptSource.getBytes()), false);
    }

    @Override
    public ScriptValue run(String method, Object... args) throws IOException
    {
        RubyScriptValue value = new RubyScriptValue(ruby.executeScript("main", name));
        return value;
    }

    @Override
    public ScriptValue run()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
