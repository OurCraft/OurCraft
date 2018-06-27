package org.craft.client;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class MouseHandler
{

    private int     dx;
    private int     dy;
    private boolean grabbed;

    public void update()
    {
        //dx = Mouse.getDX();
        //dy = Mouse.getDY();
    }

    public int getDX()
    {
        return OurCraft.getOurCraft().mouseDX;
    }

    public int getDY()
    {
        return -OurCraft.getOurCraft().mouseDY;
    }

    public void grab()
    {
        GLFW.glfwSetInputMode(OurCraft.getOurCraft().windowPointer, GLFW_CURSOR, GLFW_CURSOR_DISABLED );
        // TODO if(!grabbed)
            //Mouse.setGrabbed(true);
        grabbed = true;
    }

    public void ungrab()
    {
        GLFW.glfwSetInputMode(OurCraft.getOurCraft().windowPointer, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        // TODO if(grabbed)
        //    Mouse.setGrabbed(false);
        grabbed = false;
    }
}
