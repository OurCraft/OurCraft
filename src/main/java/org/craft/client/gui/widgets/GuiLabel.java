package org.craft.client.gui.widgets;

import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiLabel extends GuiWidget
{

    /**
     * The text to display
     */
    private String        txt;

    /**
     * The font to render {@link #displayText}
     */
    private FontRenderer  font;

    /**
     * The color in which the txt must be rendered
     */
    private int           color;

    private TextAlignment aligment;

    private float         scale;

    public GuiLabel(int id, int x, int y, String txt, FontRenderer font)
    {
        this(id, x, y, 0xFFFFFFFF, txt, font);
    }

    public GuiLabel(int id, int x, int y, int color, String txt, FontRenderer font)
    {
        super(id, x, y, 0, 0);
        this.aligment = TextAlignment.LEFT;
        this.font = font;
        this.txt = txt;
        this.color = color;
        this.scale = 1f;
    }

    public int getWidth()
    {
        font.setScale(scale);
        int textWidth = (int) font.getTextWidth(txt);
        font.setScale(1f);
        return textWidth;
    }

    public int getHeight()
    {
        font.setScale(scale);
        int textHeight = (int) font.getCharHeight('A');
        font.setScale(1f);
        return textHeight;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        if(visible)
        {
            int txtX = getX();
            int txtY = getY();
            font.setScale(scale);
            switch(aligment)
            {
                case LEFT:
                    break;
                case CENTERED:
                    txtX -= font.getTextWidth(txt) / 2;
                    txtY -= font.getCharHeight('A') / 2;
                    break;
                case RIGHT:
                    txtX -= font.getTextWidth(txt);
                    break;
            }
            font.drawShadowedString(txt, color, txtX, txtY, engine);
            font.setScale(1f);
        }
    }

    public void setText(String string)
    {
        this.txt = string;
    }

    public String getText()
    {
        return txt;
    }

    public TextAlignment getAlignment()
    {
        return aligment;
    }

    public void setAlignment(TextAlignment alignment)
    {
        this.aligment = alignment;
    }

    public void setTextScale(float f)
    {
        this.scale = f;
    }

    public float getTextScale()
    {
        return scale;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public FontRenderer getFontRenderer()
    {
        return font;
    }

}
