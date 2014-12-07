package org.craft.client.gui;

import java.io.*;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.network.*;
import org.craft.client.render.*;
import org.craft.resources.*;
import org.craft.utils.*;

public class GuiMainMenu extends Gui
{

    private ResourceLocation logoTexture = new ResourceLocation("ourcraft", "textures/logo.png");
    private String           wittyText;

    public GuiMainMenu(OurCraft game)
    {
        super(game);
        wittyText = "missigno";
        ResourceLocation loc = new ResourceLocation("ourcraft", "text/splashes.txt");
        if(oc.getAssetsLoader().doesResourceExists(loc))
        {
            try
            {
                AbstractResource res = oc.getAssetsLoader().getResource(loc);
                String[] lines = new String(res.getData(), "UTF-8").split("\n");
                int index = (int) (Math.random() * lines.length);
                wittyText = lines[index];
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void init()
    {
        addWidget(new GuiButton(0, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() / 2, 300, 40, I18n.format("main.play.singleplayer"), getFontRenderer()));
        addWidget(new GuiButton(1, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() / 2 + 60, 300, 40, I18n.format("main.play.multiplayer"), getFontRenderer()));
        addWidget(new GuiButton(2, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() / 2 + 120, 300, 40, I18n.format("main.settings"), getFontRenderer()));
        addWidget(new GuiLanguageButton(3, 0, oc.getDisplayHeight() - 40));
        addWidget(new GuiButton(4, getWidth() - 200, oc.getDisplayHeight() - 40, 200, 40, I18n.format("main.addons"), getFontRenderer()));

        addWidget(new GuiButton(10, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() / 2 + 180, 300, 40, I18n.format("main.quit"), getFontRenderer()));
    }

    @Override
    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            File worldFolder = new File(SystemUtils.getGameFolder(), "worlds");
            oc.openMenu(new GuiSelectWorld(oc, worldFolder, worldFolder.listFiles()));
        }
        else if(widget.getID() == 1)
        {
            try
            {
                oc.openMenu(new GuiConnecting(oc));
                ClientNetHandler netHandler = new ClientNetHandler(oc);
                oc.setNetHandler(netHandler);
                netHandler.connectTo("localhost", 35565);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(widget.getID() == 2)
        {
            oc.openMenu(new GuiSettings(oc, this));
        }
        else if(widget.getID() == 3)
        {
            oc.openMenu(new GuiLanguage(oc, this));
        }
        else if(widget.getID() == 4)
        {
            oc.openMenu(new GuiAddonList(oc));
        }
        else if(widget.getID() == 10)
        {
            oc.shutdown();
        }
    }

    @Override
    public void render(int mx, int my, RenderEngine renderEngine)
    {
        drawBackground(mx, my, renderEngine);
        renderEngine.bindLocation(logoTexture);
        drawTexturedRect(renderEngine, oc.getDisplayWidth() / 2 - 217, 20, 433, 240, 0, 0, 1, 1);
        super.render(mx, my, renderEngine);

        getFontRenderer().drawShadowedString(wittyText, 0xFFC0C000, (int) (oc.getDisplayWidth() - getFontRenderer().getTextWidth(wittyText)), (int) (oc.getDisplayHeight() - getFontRenderer().getCharHeight('A')), renderEngine);
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public boolean pausesGame()
    {
        return true;
    }
}
