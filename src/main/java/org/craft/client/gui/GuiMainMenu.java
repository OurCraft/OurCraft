package org.craft.client.gui;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.resources.*;
import org.craft.utils.*;

public class GuiMainMenu extends Gui
{

    private ResourceLocation logoTexture = new ResourceLocation("ourcraft", "textures/logo.png");

    public GuiMainMenu(FontRenderer font)
    {
        super(font);
    }

    @Override
    public void init()
    {
        addWidget(new GuiButton(0, 0, 0, 100, 100, "Test", getFontRenderer()));
    }

    public void actionPerformed(GuiWidget widget)
    {
        Log.message("Clicked on widget with id " + widget.getID());
    }

    @Override
    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        super.draw(mx, my, renderEngine);
        renderEngine.bindLocation(logoTexture);
        drawTexturedRect(renderEngine, OurCraft.getOurCraft().getDisplayWidth() / 2 - 350, 0, 700, 150, 0, 0, 1, 1);
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

}
