package org.craft.client.gui.widgets;

import org.craft.client.gui.*;

public class GuiPopupMenu extends GuiList<GuiPopupElement>
{

    private Gui parent;

    public GuiPopupMenu(Gui gui)
    {
        super(-42, 0, 0, 20, 20, 20);
        setScrollable(false);
        showScrollBar(false);
        this.parent = gui;
    }

    public void pack()
    {
        int maxWidth = 20;
        int maxHeight = 20;
        for(int index = 0; index < getSize(); index++ )
        {
            GuiPopupElement elem = getSlot(index);
            if(maxWidth < elem.getWidth())
            {
                maxWidth = elem.getWidth();
            }

            if(maxHeight < elem.getHeight())
            {
                maxHeight = elem.getHeight();
            }
        }

        setSlotHeight(maxHeight);
        setWidth(maxWidth);
        setHeight(maxHeight * getSize());
    }

    public void onClicked(GuiPopupElement elem)
    {
        parent.onPopupMenuClicked(elem);
    }

}
