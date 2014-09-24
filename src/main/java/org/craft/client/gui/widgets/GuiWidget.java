package org.craft.client.gui.widgets;

public class GuiWidget
{

    private int id;
    private int x;
    private int y;
    private int w;
    private int h;

    public GuiWidget(int id, int x, int y, int w, int h)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getID()
    {
        return id;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return w;
    }

    public int getHeight()
    {
        return h;
    }
}
