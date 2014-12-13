package org.craft.client.gui.widgets;

import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiSlider extends GuiWidget
{

    private float        value;
    private float        rawValue;
    private float        rangeMin;
    private float        rangeMax;
    private boolean      pressed;
    private FontRenderer fontRenderer;

    public GuiSlider(int id, int x, int y, int w, int h, float rangeMin, float rangeMax, FontRenderer fontRenderer)
    {
        super(id, x, y, w, h);
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.fontRenderer = fontRenderer;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        engine.bindLocation(Gui.widgetsTexture);
        Gui.drawTexturedRect(engine, getX(), getY(), getWidth() / 2, getHeight(), 0, 40f / 256f, 0 + 50f / 256f, 60f / 256f);
        Gui.drawTexturedRect(engine, getX() + getWidth() / 2, getY(), getWidth() / 2, getHeight(), 0 + 50f / 256f, 40f / 256f, 0 + 100f / 256f, 60f / 256f);

        float minU = pressed ? 110f / 256f : 100f / 256f;
        Gui.drawTexturedRect(engine, (int) (getX() + (getWidth() - 10) * rawValue), getY(), 10, getHeight(), minU, 40f / 256f, minU + 10 / 256f, 60f / 256f);
        fontRenderer.drawShadowedString("" + value, 0xFFFFFFFF, getX() + getWidth() / 2 - (int) fontRenderer.getTextWidth("" + value) / 2, (int) (getY() + getHeight() / 2 - fontRenderer.getCharHeight('A') / 2), engine);
    }

    public float getRangeMin()
    {
        return rangeMin;
    }

    public float getRangeMax()
    {
        return rangeMax;
    }

    public void setRangeMin(float rangeMin)
    {
        this.rangeMin = rangeMin;
    }

    public void setRangeMax(float rangeMax)
    {
        this.rangeMax = rangeMax;
    }

    @Override
    public boolean onButtonPressed(int x, int y, int button)
    {
        if(isMouseOver(x, y))
            pressed = true;
        return true;
    }

    @Override
    public boolean onButtonReleased(int x, int y, int button)
    {
        pressed = false;
        return true;
    }

    public boolean isPressed()
    {
        return pressed;
    }

    @Override
    public boolean handleMouseMovement(int mx, int my, int dx, int dy)
    {
        if(pressed)
        {
            float newValue = (float) (mx - getX()) / (float) getWidth();
            if(newValue < 0.f)
                newValue = 0.f;
            else if(newValue > 1.f)
                newValue = 1.f;
            rawValue = newValue;
            value = (getRangeMax() - getRangeMin()) * rawValue;
            return true;
        }
        return false;
    }

    public float getRawValue()
    {
        return rawValue;
    }

    public float getValue()
    {
        return value;
    }

    public void setRawValue(float value)
    {
        this.rawValue = value;
        if(rawValue < 0.f)
            rawValue = 0.f;
        else if(rawValue > 1.f)
            rawValue = 1.f;
        this.value = (getRangeMax() - getRangeMin()) * rawValue;
    }

    public void setValue(float value)
    {
        this.value = value;
        rawValue = value / (getRangeMax() - getRangeMin());
        if(rawValue < 0.f)
            rawValue = 0.f;
        else if(rawValue > 1.f)
            rawValue = 1.f;
    }
}
