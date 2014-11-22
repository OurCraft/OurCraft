package org.craft.client.gui.widgets;

import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiButton extends GuiWidget
{

    /**
     * The text to display
     */
    private String       displayText;

    /**
     * The font to render {@link #displayText}
     */
    private FontRenderer font;

    /**
     * A boolean to know if this button is pressed
     */
    private boolean      pressed;

    public GuiButton(int id, int x, int y, int w, int h, String txt, FontRenderer font)
    {
        super(id, x, y, w, h);
        this.displayText = txt;
        this.font = font;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        if(visible)
        {
            engine.bindLocation(Gui.widgetsTexture);
            float minU = pressed ? 100f / 256f : 0f;
            Gui.drawTexturedRect(engine, getX(), getY(), getWidth() / 2, getHeight(), minU, 0, minU + 50f / 256f, 20f / 256f);
            Gui.drawTexturedRect(engine, getX() + getWidth() / 2, getY(), getWidth() / 2, getHeight(), minU + 50f / 256f, 0, minU + 100f / 256f, 20f / 256f);
            int color = 0xFFFFFFFF;
            if(!enabled)
            {
                color = 0xFF707070;
            }
            else if(isMouseOver(mx, my) || pressed)
            {
                color = 0xFFFFF544;
            }
            if(font != null)
                font.drawString(displayText, color, (int) (getX() + getWidth() / 2 - font.getTextWidth(displayText) / 2), (int) (getY() + getHeight() / 2 - font.getCharHeight(' ') / 2), engine);
        }
    }

    public boolean onButtonPressed(int x, int y, int button)
    {
        if(isMouseOver(x, y))
            pressed = true;
        return pressed;
    }

    public boolean onButtonReleased(int x, int y, int button)
    {
        pressed = false;
        return isMouseOver(x, y);
    }

    public boolean isPressed()
    {
        return pressed;
    }

    public void setText(String string)
    {
        this.displayText = string;
    }

    public String getText()
    {
        return displayText;
    }

}
