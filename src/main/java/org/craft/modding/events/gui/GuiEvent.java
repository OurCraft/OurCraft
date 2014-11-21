package org.craft.modding.events.gui;

import org.craft.*;
import org.craft.client.gui.widgets.*;
import org.craft.modding.events.*;

public abstract class GuiEvent extends ModEvent
{

    private GuiPanel menu;

    public GuiEvent(OurCraftInstance instance, GuiPanel menu)
    {
        super(instance);
        this.menu = menu;
    }

    public GuiPanel getMenu()
    {
        return menu;
    }

}
