package org.craft.client.gui;

import java.io.*;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.network.*;
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
        addWidget(new GuiButton(0, OurCraft.getOurCraft().getDisplayWidth() / 2 - 150, OurCraft.getOurCraft().getDisplayHeight() / 2 - 20, 300, 40, I18n.format("main.play.singleplayer"), getFontRenderer()));
        addWidget(new GuiButton(1, OurCraft.getOurCraft().getDisplayWidth() / 2 - 150, OurCraft.getOurCraft().getDisplayHeight() / 2 + 40, 300, 40, I18n.format("main.play.multiplayer"), getFontRenderer()));
        addWidget(new GuiButton(10, OurCraft.getOurCraft().getDisplayWidth() / 2 - 150, OurCraft.getOurCraft().getDisplayHeight() / 2 + 100, 300, 40, I18n.format("main.quit"), getFontRenderer()));
    }

    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            File worldFolder = new File(SystemUtils.getGameFolder(), "worlds");
            OurCraft.getOurCraft().openMenu(new GuiSelectWorld(getFontRenderer(), worldFolder, worldFolder.listFiles()));
        }
        else if(widget.getID() == 1)
        {
            try
            {
                OurCraft.getOurCraft().openMenu(new GuiConnecting(getFontRenderer()));
                ClientNetHandler netHandler = new ClientNetHandler();
                OurCraft.getOurCraft().setNetHandler(netHandler);
                netHandler.connectTo("localhost", 35565);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(widget.getID() == 10)
        {
            OurCraft.getOurCraft().shutdown();
        }
    }

    @Override
    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        drawBackground(mx, my, renderEngine);
        renderEngine.bindLocation(logoTexture);
        drawTexturedRect(renderEngine, OurCraft.getOurCraft().getDisplayWidth() / 2 - 350, 0, 700, 150, 0, 0, 1, 1);
        super.draw(mx, my, renderEngine);
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
