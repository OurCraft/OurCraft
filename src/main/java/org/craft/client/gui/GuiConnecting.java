package org.craft.client.gui;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiConnecting extends Gui
{

    private String    status = "Establishing connexion";
    private int       updateCounter;
    private GuiButton gobackButton;

    public GuiConnecting(FontRenderer font)
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
        gobackButton = new GuiButton(0, OurCraft.getOurCraft().getDisplayWidth() / 2 - 100, OurCraft.getOurCraft().getDisplayHeight() / 2 + 50, 200, 40, I18n.format("menu.back"), getFontRenderer());
        gobackButton.visible = false;
        gobackButton.enabled = false;
        addWidget(gobackButton);
    }

    @Override
    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            OurCraft.getOurCraft().openMenu(new GuiMainMenu(getFontRenderer()));
        }
    }

    @Override
    public void update()
    {
        updateCounter++ ;
    }

    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        drawBackground(mx, my, renderEngine);
        super.draw(mx, my, renderEngine);
        getFontRenderer().drawShadowedString(status, 0xFFFFFFFF, OurCraft.getOurCraft().getDisplayWidth() / 2 - (int) getFontRenderer().getTextLength(status) / 2, OurCraft.getOurCraft().getDisplayHeight() / 2 - (int) getFontRenderer().getCharHeight('A') / 2 - 50, renderEngine);

        int length = 11;
        String finalLoadingTxt = "";
        for(int i = 0; i < length; i++ )
        {
            int index = (updateCounter / 3) % (length * 2);
            if(index >= length)
                index = length * 2 - index;
            if(i == index)
            {
                finalLoadingTxt += TextFormatting.generateFromColor(128, 128, 128) + "o";
            }
            else if(i == index - 1)
            {
                finalLoadingTxt += TextFormatting.generateFromColor(128, 128, 128) + "\\";
            }
            else if(i == index + 1)
            {
                finalLoadingTxt += TextFormatting.generateFromColor(128, 128, 128) + "/";
            }
            else
                finalLoadingTxt += TextFormatting.generateFromColor(180, 180, 180) + "_";
        }
        getFontRenderer().drawString(finalLoadingTxt, 0xFF000000, (int) (OurCraft.getOurCraft().getDisplayWidth() / 2 - getFontRenderer().getTextLength(finalLoadingTxt) / 2), (int) (OurCraft.getOurCraft().getDisplayHeight() / 2 - getFontRenderer().getCharHeight('A') / 2), renderEngine);
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void showGoBackButton()
    {
        gobackButton.visible = true;
        gobackButton.enabled = true;
    }

}
