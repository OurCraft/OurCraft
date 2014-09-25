package org.craft.client.gui;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.fonts.*;
import org.lwjgl.input.*;

public class GuiPauseMenu extends Gui
{

    public GuiPauseMenu(FontRenderer font)
    {
        super(font);
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        addWidget(new GuiButton(0, OurCraft.getOurCraft().getDisplayWidth() / 2 - 150, OurCraft.getOurCraft().getDisplayHeight() / 2, 300, 40, I18n.format("main.play.return"), getFontRenderer()));
        addWidget(new GuiButton(1, OurCraft.getOurCraft().getDisplayWidth() / 2 - 150, OurCraft.getOurCraft().getDisplayHeight() / 2 + 60, 300, 40, I18n.format("main.play.quitToMainScreen"), getFontRenderer()));
    }

    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            OurCraft.getOurCraft().openMenu(new GuiIngame(getFontRenderer()));
        }
        else if(widget.getID() == 1)
        {
            OurCraft.getOurCraft().quitToMainScreen();
        }
    }

    @Override
    public void update()
    {

    }

    public void keyReleased(int id, char c)
    {
        super.keyReleased(id, c);
        if(id == Keyboard.KEY_ESCAPE)
        {
            OurCraft.getOurCraft().openMenu(new GuiIngame(getFontRenderer()));
        }
    }

    public boolean pausesGame()
    {
        return true;
    }

}
