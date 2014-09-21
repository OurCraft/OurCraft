package org.craft.client;

import org.lwjgl.input.*;

public class MouseHandler
{

    private int dx;
    private int dy;

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
        Mouse.setGrabbed(true);
    }

    public void ungrab()
    {
        Mouse.setGrabbed(false);
    }
}
