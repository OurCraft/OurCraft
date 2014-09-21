package org.craft.launch;

import java.io.*;

import org.craft.client.*;

public class OurCraftLauncher
{

    public static void main(String[] args)
    {
        try
        {
            OurCraft instance = new OurCraft();
            LWJGLSetup.load(new File(instance.getGameFolder(), "natives"));
            instance.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
