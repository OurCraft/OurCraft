package org.craft.client;

import org.lwjgl.input.*;

public class MouseHandler
{

    private int     dx;
    private int     dy;
    private boolean grabbed;

    public void update()
    {
        dx = Mouse.getDX();
        dy = Mouse.getDY();
    }

    public int getDX()
    {
        return dx;
    }

    public int getDY()
    {
        return dy;
    }

    public void grab()
    {
        if(!grabbed)
            Mouse.setGrabbed(true);
        grabbed = true;
    }

    public void ungrab()
    {
        if(grabbed)
            Mouse.setGrabbed(false);
        grabbed = false;
    }
}
