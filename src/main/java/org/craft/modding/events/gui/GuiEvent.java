package org.craft.modding.events.gui;

import org.craft.*;
import org.craft.client.gui.*;
import org.craft.modding.events.*;

public abstract class GuiEvent extends ModEvent
{

    private Gui menu;

    public GuiEvent(OurCraftInstance instance, Gui menu)
    {
        super(instance);
        this.menu = menu;
    }

    public Gui getMenu()
    {
        return menu;
    }

}
