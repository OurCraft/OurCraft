package org.craft.client.gui.widgets;

import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiButton extends GuiWidget
{

    private String       displayText;
    private FontRenderer font;

    public GuiButton(int id, int x, int y, int w, int h, String txt, FontRenderer font)
    {
        super(id, x, y, w, h);
        this.displayText = txt;
        this.font = font;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        engine.bindLocation(Gui.widgetsTexture);
        Gui.drawTexturedRect(engine, getX(), getY(), getWidth(), getHeight(), 0, 0, 100f / 256f, 10f / 256f);
        int color = 0xFFFFFF;
        if(isMouseOver(mx, my))
        {
            color = 0xFFF544;
        }
        font.drawShadowedString(displayText, color, (int) (getX() + getWidth() / 2 - font.getTextLength(displayText) / 2), (int) (getY() + getHeight() / 2 - font.getCharHeight(' ')), engine);
    }

}
