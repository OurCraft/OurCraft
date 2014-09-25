package org.craft.client.gui;

import java.util.*;

import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.maths.*;
import org.craft.resources.*;

public abstract class Gui
{

    public static ResourceLocation widgetsTexture = new ResourceLocation("ourcraft", "textures/gui/widgets.png");
    private static OpenGLBuffer    buffer         = new OpenGLBuffer();
    private FontRenderer           fontRenderer;
    private ArrayList<GuiWidget>   widgets;

    public Gui(FontRenderer font)
    {
        widgets = new ArrayList<GuiWidget>();
        this.fontRenderer = font;
    }

    public void addWidget(GuiWidget widget)
    {
        widgets.add(widget);
    }

    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    public static void drawTexturedRect(RenderEngine engine, int x, int y, int w, int h, float minU, float minV, float maxU, float maxV)
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

    public void actionPerformed(GuiWidget widget)
    {
    }

    public void keyPressed(int id, char c)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.keyPressed(id, c))
            {
                break;
            }
        }
    }

    public void keyReleased(int id, char c)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.keyReleased(id, c))
            {
                break;
            }
        }
    }

    public void handleClick(int x, int y, int button)
    {
        if(button == 0)
            for(GuiWidget widget : widgets)
            {
                if(widget.isMouseOver(x, y))
                {
                    actionPerformed(widget);
                    break;
                }
            }
    }

    public boolean pausesGame()
    {
        return false;
    }

    public abstract boolean requiresMouse();

    public abstract void init();

    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        for(GuiWidget widget : widgets)
        {
            widget.render(mx, my, renderEngine);
        }
    }

    public abstract void update();
}
