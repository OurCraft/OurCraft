package org.craft.modding.script.lua;

import org.craft.utils.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class PrintFunction extends LibFunction
{

    private Globals globals;

    public PrintFunction(Globals globals)
    {
        this.globals = globals;
    }

    @Override
    public LuaValue call(LuaValue arg)
    {
        try
        {
            String toPrint = "";
            for(int i = 0; i < arg.narg(); i++ )
            {
                if(i != 0)
                    toPrint += "\t";
                toPrint += arg.arg(i + 1);
            }
            String scriptName = globals.get("debug").get("getinfo").call("1").get("source").toString();
            Log.message("[Script " + scriptName + "] " + toPrint);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return LuaValue.NIL;
    }

}
