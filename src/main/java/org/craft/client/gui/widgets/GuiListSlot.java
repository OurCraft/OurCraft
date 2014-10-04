package org.craft.client.gui.widgets;

import org.craft.client.render.*;

public abstract class GuiListSlot
{
    public abstract void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner);
}
