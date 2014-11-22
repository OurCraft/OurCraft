package org.craft.modding.events.gui;

import org.craft.*;
import org.craft.client.gui.*;
import org.craft.client.gui.widgets.*;

public abstract class GuiBuildingEvent extends GuiEvent
{

    public abstract static class PopupMenu extends GuiEvent
    {

        private GuiPopupMenu popupMenu;

        public PopupMenu(OurCraftInstance instance, GuiPanel menu, GuiPopupMenu popupMenu)
        {
            super(instance, menu);
            this.popupMenu = popupMenu;
        }

        public GuiPopupMenu getPopupMenu()
        {
            return popupMenu;
        }

        public static class Pre extends PopupMenu
        {

            public Pre(OurCraftInstance instance, GuiPanel menu, GuiPopupMenu popupMenu)
            {
                super(instance, menu, popupMenu);
            }

            @Override
            public boolean isCancellable()
            {
                return true;
            }
        }

        public static class Post extends PopupMenu
        {

            public Post(OurCraftInstance instance, GuiPanel menu, GuiPopupMenu popupMenu)
            {
                super(instance, menu, popupMenu);
            }

            @Override
            public boolean isCancellable()
            {
                return false;
            }
        }

    }

    public final static class Pre extends GuiBuildingEvent
    {

        public Pre(OurCraftInstance instance, Gui menu)
        {
            super(instance, menu);
        }

        @Override
        public boolean isCancellable()
        {
            return true;
        }
    }

    public final static class Post extends GuiBuildingEvent
    {

        public Post(OurCraftInstance instance, Gui menu)
        {
            super(instance, menu);
        }

        @Override
        public boolean isCancellable()
        {
            return false;
        }
    }

    public GuiBuildingEvent(OurCraftInstance instance, Gui menu)
    {
        super(instance, menu);
    }

}
