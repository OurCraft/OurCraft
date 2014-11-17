package org.craft.modding.events.gui;

import org.craft.*;
import org.craft.client.gui.*;

public abstract class GuiBuildingEvent extends GuiEvent
{

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
