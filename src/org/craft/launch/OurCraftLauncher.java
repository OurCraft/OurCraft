package org.craft.launch;

import java.io.*;

import org.craft.client.*;

public class OurCraftLauncher
{

    public static void main(String[] args)
    {
        OurCraft instance = new OurCraft();
        try
        {
            LWJGLSetup.load(new File(instance.getGameFolder(), "natives"));
            instance.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
