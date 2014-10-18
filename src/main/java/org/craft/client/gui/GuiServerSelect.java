package org.craft.client.gui;

import org.craft.client.OurCraft;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiServerSelect extends Gui
{

    public class GuiServerSlot extends GuiListSlot
    {

        @Override
        public void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner)
        {

        }

    }

    private GuiList<GuiServerSlot> serverList;

    public GuiServerSelect(OurCraft game)
    {
        super(game);
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        serverList = new GuiList<GuiServerSlot>(0, 0, 0, 200, 200, 20);
        addWidget(serverList);
        // TODO
    }

    @Override
    public void update()
    {

    }

}
