package org.craft.client.gui.widgets;

import org.craft.client.render.*;

public abstract class GuiWidget
{

    private int    id;
    private int    x;
    private int    y;
    private int    w;
    private int    h;
    public boolean visible;
    public boolean enabled;

    public GuiWidget(int id, int x, int y, int w, int h)
    {
        visible = true;
        enabled = true;
        this.id = id;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public abstract void render(int mx, int my, RenderEngine engine);

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

    public boolean isMouseOver(int x, int y)
    {
        return getX() <= x && getX() + getWidth() > x && getY() <= y && getY() + getHeight() > y;
    }

    public boolean keyPressed(int id, char c)
    {
        return false;
    }

    public boolean keyReleased(int id, char c)
    {
        return false;
    }

    public boolean onButtonReleased(int x, int y, int button)
    {
        return true;
    }

    public boolean onButtonPressed(int x, int y, int button)
    {
        return true;
    }
}
