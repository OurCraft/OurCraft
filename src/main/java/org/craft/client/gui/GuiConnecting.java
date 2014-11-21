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

    public GuiConnecting(OurCraft game)
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
        gobackButton = new GuiButton(0, oc.getDisplayWidth() / 2 - 100, oc.getDisplayHeight() / 2 + 50, 200, 40, I18n.format("menu.back"), getFontRenderer());
        gobackButton.visible = false;
        gobackButton.enabled = false;
        addWidget(gobackButton);
    }

    @Override
    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            oc.openMenu(new GuiMainMenu(oc));
        }
    }

    @Override
    public void update()
    {
        super.update();
        updateCounter++ ;
    }

    public void render(int mx, int my, RenderEngine renderEngine)
    {
        drawBackground(mx, my, renderEngine);
        super.render(mx, my, renderEngine);
        getFontRenderer().drawShadowedString(status, 0xFFFFFFFF, oc.getDisplayWidth() / 2 - (int) getFontRenderer().getTextWidth(status) / 2, oc.getDisplayHeight() / 2 - (int) getFontRenderer().getCharHeight('A') / 2 - 50, renderEngine);

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
        getFontRenderer().drawString(finalLoadingTxt, 0xFF000000, (int) (oc.getDisplayWidth() / 2 - getFontRenderer().getTextWidth(finalLoadingTxt) / 2), (int) (oc.getDisplayHeight() / 2 - getFontRenderer().getCharHeight('A') / 2), renderEngine);
    }

    /**
     * Sets status written on gui
     */
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
