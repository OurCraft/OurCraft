package org.craft;

import java.io.*;

import org.craft.launch.*;
import org.lwjgl.*;

public class OurCraftLauncher
{

    public static void main(String[] args)
    {
        OurCraftMain instance = new OurCraftMain();
        try
        {
            LWJGLSetup.load(new File(instance.getGameFolder(), "natives"));
            Sys.alert("IT'S ALIVE!!", "OurCraft setup seems to be ready!!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
