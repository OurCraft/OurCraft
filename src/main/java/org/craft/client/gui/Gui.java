package org.craft.client.gui;

import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.maths.*;

public abstract class Gui
{

    private FontRenderer fontRenderer;
    private OpenGLBuffer buffer;

    public Gui(FontRenderer font)
    {
        this.fontRenderer = font;
        buffer = new OpenGLBuffer();
    }

    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    public void drawTexturedRect(RenderEngine engine, int x, int y, int w, int h, float minU, float minV, float maxU, float maxV)
    {
        buffer.addVertex(new Vertex(Vector3.get(x, y, 0), Vector2.get(minU, minV)));
        buffer.addVertex(new Vertex(Vector3.get(x + w, y, 0), Vector2.get(maxU, minV)));
        buffer.addVertex(new Vertex(Vector3.get(x + w, y + h, 0), Vector2.get(maxU, maxV)));
        buffer.addVertex(new Vertex(Vector3.get(x, y + h, 0), Vector2.get(minU, maxV)));

        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);

        buffer.addIndex(2);
        buffer.addIndex(3);
        buffer.addIndex(0);

        buffer.upload();
        engine.renderBuffer(buffer);
        buffer.clearAndDisposeVertices();
    }

    public abstract boolean requiresMouse();

    public abstract void init();

    public abstract void draw(int mx, int my, RenderEngine renderEngine);

    public abstract void update();
}
