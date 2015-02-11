package org.craft.client.render;

import org.craft.client.render.texture.*;

public class FramebufferInfo
{

    private int          id;
    private int          width;
    private int          height;
    private OpenGLBuffer buffer;
    private Texture      colorBuffer;

    public FramebufferInfo(int framebufferID, int width, int height, Texture colorBuffer, OpenGLBuffer buffer)
    {
        this.id = framebufferID;
        this.width = width;
        this.height = height;
        this.colorBuffer = colorBuffer;
        this.buffer = buffer;
    }

    public Texture getColorBuffer()
    {
        return colorBuffer;
    }

    public OpenGLBuffer getBuffer()
    {
        return buffer;
    }

    public int getID()
    {
        return id;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
