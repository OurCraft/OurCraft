package org.craft.client.gui.widgets;

import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.resources.*;

public class GuiIconButton extends GuiSmallButton
{

    private ResourceLocation location;
    private float            minU;
    private float            maxU;
    private float            minV;
    private float            maxV;

    public GuiIconButton(int id, int x, int y, ResourceLocation location)
    {
        this(id, x, y, location, 0, 0, 1, 1);
    }

    public GuiIconButton(int id, int x, int y, ResourceLocation location, float minU, float minV, float maxU, float maxV)
    {
        super(id, x, y, "", null);
        this.location = location;
        this.minU = minU;
        this.maxU = maxU;
        this.minV = minV;
        this.maxV = maxV;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        if(visible)
        {
            engine.bindLocation(location);
            Gui.drawTexturedRect(engine, getX(), getY(), getWidth(), getHeight(), minU, minV, maxU, maxV);
        }
    }

}
