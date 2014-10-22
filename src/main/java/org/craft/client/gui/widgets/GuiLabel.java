package org.craft.client.gui.widgets;

import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiLabel extends GuiWidget
{

    /**
     * The text to display
     */
    private String       txt;

    /**
     * The font to render {@link #displayText}
     */
    private FontRenderer font;

    /**
     * The color in which the txt must be rendered
     */
    private int          color;

    public GuiLabel(int id, int x, int y, String txt, FontRenderer font)
    {
        this(id, x, y, 0xFFFFFFFF, txt, font);
    }

    public GuiLabel(int id, int x, int y, int color, String txt, FontRenderer font)
    {
        super(id, x, y, 0, 0);
        this.font = font;
        this.txt = txt;
        this.color = color;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        if(visible)
        {
            font.drawShadowedString(txt, color, getX(), getY(), engine);
        }
    }

}
