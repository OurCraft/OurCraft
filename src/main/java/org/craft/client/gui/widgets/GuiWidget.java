package org.craft.client.gui.widgets;

import org.craft.client.render.*;

public abstract class GuiWidget
{

    /**
     * The id of the widget
     */
    private int    id;

    private int    x;

    private int    y;

    private int    w;

    private int    h;

    /**
     * This boolean decides whether or not this widget will be drawn.<br/>The implementation might ignore it.
     */
    public boolean visible;

    /**
     * This boolean decides whether or not this widget can be clicked/focused.
     */
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

    /**
     * Renders the widget
     * @param mx : Mouse coord on X
     * @param my : Mouse coord on Y
     * @param engine : the RenderEngine
     */
    public abstract void render(int mx, int my, RenderEngine engine);

    /**
     * Returns the id assigned to the widget
     */
    public int getID()
    {
        return id;
    }

    /**
     * Returns position on X of this widget
     */
    public int getX()
    {
        return x;
    }

    /**
     * Returns position on Y of this widget
     */
    public int getY()
    {
        return y;
    }

    /**
     * Returns width of this widget
     */
    public int getWidth()
    {
        return w;
    }

    /**
     * Returns height of this widget
     */
    public int getHeight()
    {
        return h;
    }

    /**
     * Returns true if given mouse coords are inside this widget
     */
    public boolean isMouseOver(int x, int y)
    {
        return getX() <= x && getX() + getWidth() > x && getY() <= y && getY() + getHeight() > y;
    }

    /**
     * Handle a keyPressed event. Returns true if event was consumed
     */
    public boolean keyPressed(int id, char c)
    {
        return false;
    }

    /**
     * Handle a keyReleased event. Returns true if event was consumed
     */
    public boolean keyReleased(int id, char c)
    {
        return false;
    }

    /**
     * Handle a 'mouse button released' event. Returns true if event was consumed
     */
    public boolean onButtonReleased(int x, int y, int button)
    {
        return true;
    }

    /**
     * Handle a 'mouse button pressed' event. Returns true if event was consumed
     */
    public boolean onButtonPressed(int x, int y, int button)
    {
        return true;
    }

    /**
     * Handle a 'mouse wheel moved' event. Returns true if event was consumed
     */
    public boolean handleMouseWheelMovement(int mx, int my, int deltaWheel)
    {
        return false;
    }

    /**
     * Sets the location of this widget at given coords
     */
    public void setLocation(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Handles a 'mouse moved' event. Returns true if event was consumed
     */
    public boolean handleMouseMovement(int mx, int my, int dx, int dy)
    {
        return false;
    }

    public void setWidth(int w)
    {
        this.w = w;
    }

    public void setHeight(int h)
    {
        this.h = h;
    }
}
