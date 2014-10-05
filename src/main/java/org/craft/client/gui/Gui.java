package org.craft.client.gui;

import java.io.*;
import java.util.*;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.maths.*;
import org.craft.resources.*;

public abstract class Gui
{

    public static ResourceLocation widgetsTexture = new ResourceLocation("ourcraft", "textures/gui/widgets.png");
    private static OpenGLBuffer    buffer         = new OpenGLBuffer();
    private static Texture         backgroundTexture;
    private FontRenderer           fontRenderer;
    private ArrayList<GuiWidget>   widgets;

    public Gui(FontRenderer font)
    {
        if(backgroundTexture == null)
        {
            try
            {
                backgroundTexture = OpenGLHelper.loadTexture(OurCraft.getOurCraft().getAssetsLoader().getResource(new ResourceLocation("ourcraft", "textures/gui/background.png")));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
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

    private static Vector3 bottomLeftCornerPos  = Vector3.get(0, 0, 0);
    private static Vector3 bottomRightCornerPos = Vector3.get(0, 0, 0);
    private static Vector3 topLeftCornerPos     = Vector3.get(0, 0, 0);
    private static Vector3 topRightCornerPos    = Vector3.get(0, 0, 0);

    private static Vector2 bottomLeftCornerUV   = Vector2.get(0, 0);
    private static Vector2 bottomRightCornerUV  = Vector2.get(0, 0);
    private static Vector2 topLeftCornerUV      = Vector2.get(0, 0);
    private static Vector2 topRightCornerUV     = Vector2.get(0, 0);

    static
    {
        buffer.addVertex(new Vertex(bottomLeftCornerPos, bottomLeftCornerUV));
        buffer.addVertex(new Vertex(bottomRightCornerPos, bottomRightCornerUV));
        buffer.addVertex(new Vertex(topLeftCornerPos, topLeftCornerUV));
        buffer.addVertex(new Vertex(topRightCornerPos, topRightCornerUV));
        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);

        buffer.addIndex(2);
        buffer.addIndex(3);
        buffer.addIndex(0);
    }

    public static void drawTexturedRect(RenderEngine engine, int x, int y, int w, int h, float minU, float minV, float maxU, float maxV)
    {
        bottomLeftCornerPos.set(x, y, 0);
        bottomRightCornerPos.set(x + w, y, 0);
        topLeftCornerPos.set(x + w, y + h, 0);
        topRightCornerPos.set(x, y + h, 0);
        bottomLeftCornerUV.set(minU, minV);
        bottomRightCornerUV.set(maxU, minV);
        topLeftCornerUV.set(maxU, maxV);
        topRightCornerUV.set(minU, maxV);
        buffer.upload();
        engine.renderBuffer(buffer);
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

    public void handleButtonReleased(int x, int y, int button)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.isMouseOver(x, y))
            {
                if(widget.enabled)
                {
                    widget.onButtonReleased(x, y, button);
                    if(button == 0)
                        actionPerformed(widget);
                }
            }
        }
    }

    public void handleButtonPressed(int x, int y, int button)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.isMouseOver(x, y))
            {
                if(widget.enabled)
                    widget.onButtonPressed(x, y, button);
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

    public void drawBackground(int mx, int my, RenderEngine renderEngine)
    {
        renderEngine.bindTexture(backgroundTexture, 0);
        drawTexturedRect(renderEngine, 0, 0, OurCraft.getOurCraft().getDisplayWidth(), OurCraft.getOurCraft().getDisplayHeight(), 0, 0, OurCraft.getOurCraft().getDisplayWidth() / backgroundTexture.getWidth(), OurCraft.getOurCraft().getDisplayHeight() / backgroundTexture.getHeight());
    }

    public abstract void update();
}
