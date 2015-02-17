package org.craft.client.gui.widgets;

import org.craft.client.render.*;

public abstract class GuiListSlot
{
    /**
     * Render this slot at given (x,y)
     */
    public abstract void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner);

    /**
     * Handles an event where the mouse wheel is moved
     */
    public boolean onMouseWheelMoved(int index, int x, int y, int w, int h, int mx, int my, int dwheel, GuiList<?> owner)
    {
        return false;
    }

    /**
     * Handles an event where a mouse button is pressed
     */
    public void onButtonPressed(int index, int x, int y, int w, int h, int mx, int my, int button, GuiList<?> owner)
    {
        ;
    }

    /**
     * Handles an event where a mouse button is released
     */
    public void onButtonReleased(int index, int x, int y, int w, int h, int mx, int my, int button, GuiList<?> owner)
    {
        ;
    }

    /**
     * Handles an event where the mouse is moved
     */
    public void handleMouseMovement(int index, int x, int y, int w, int h, int mx, int my, int dx, int dy)
    {
        ;
    }

    public boolean isMouseOver(int mx, int my, int x, int y, int w, int h)
    {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    private Object data;

    /**
     * Stores an user-provided object to be retrieved from the slot
     * @param userData Object provided by the user to be saved in this slot
     * @see #getData()
     */
    public void setData(Object userData)
    {
        this.data = userData;
    }

    /**
     * Gets the user-provided object saved in this slot. 
     * @return Object provided by the user saved in this slot.
     * @see #setData(Object)
     */
    public Object getData()
    {
        return data;
    }
}
