package org.craft.client.gui.widgets;

import org.craft.client.render.*;

public abstract class GuiListSlot
{
    /**
     * Render this slot at given (x,y)
     */
    public abstract void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner);

    /**
     * Handles an event where a mouse button is pressed
     */
    public void onButtonPressed(int index, int x, int y, int w, int h, int mx, int my, int button)
    {
        ;
    }

    /**
     * Handles an event where a mouse button is released
     */
    public void onButtonReleased(int index, int x, int y, int w, int h, int mx, int my, int button)
    {
        ;
    }

}
