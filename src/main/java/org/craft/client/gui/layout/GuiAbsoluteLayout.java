package org.craft.client.gui.layout;

import org.craft.client.gui.widgets.*;

public class GuiAbsoluteLayout implements IGuiLayout
{
    @Override
    public void onAdd(GuiWidget widget, GuiPanel container)
    {
        widget.setLocation(widget.getX() + container.getX(), widget.getY() + container.getY());
    }

}
