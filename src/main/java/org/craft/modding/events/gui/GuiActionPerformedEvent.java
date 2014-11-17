package org.craft.modding.events.gui;

import org.craft.*;
import org.craft.client.gui.*;
import org.craft.client.gui.widgets.*;

public class GuiActionPerformedEvent extends GuiEvent
{

    private GuiWidget widget;

    public GuiActionPerformedEvent(OurCraftInstance instance, Gui menu, GuiWidget widget)
    {
        super(instance, menu);
        this.widget = widget;
    }

    public GuiWidget getWidget()
    {
        return widget;
    }

    @Override
    public boolean isCancellable()
    {
        return true;
    }

}
