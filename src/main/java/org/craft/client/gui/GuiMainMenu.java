package org.craft.client.gui;

import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.resources.*;

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

    }

    @Override
    public void draw(int mx, int my, RenderEngine renderEngine)
    {
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
