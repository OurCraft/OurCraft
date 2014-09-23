package org.craft.client.gui;

import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public abstract class Gui
{

    private FontRenderer fontRenderer;

    public Gui(FontRenderer font)
    {
        this.fontRenderer = font;
    }

    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    public abstract void init();

    public abstract void draw(int mx, int my, RenderEngine renderEngine);

    public abstract void update();
}
