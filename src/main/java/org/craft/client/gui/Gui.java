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
    private GuiWidget              selectedWidget;

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

    /**
     * Adds given widget to this gui
     */
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
        buffer.addVertex(Vertex.get(bottomLeftCornerPos, bottomLeftCornerUV));
        buffer.addVertex(Vertex.get(bottomRightCornerPos, bottomRightCornerUV));
        buffer.addVertex(Vertex.get(topLeftCornerPos, topLeftCornerUV));
        buffer.addVertex(Vertex.get(topRightCornerPos, topRightCornerUV));
        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);

        buffer.addIndex(2);
        buffer.addIndex(3);
        buffer.addIndex(0);
    }

    /**
     * Draws textured rect at given coordinates
     */
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

    /*
     * Method called when a widget is clicked
     */
    public void actionPerformed(GuiWidget widget)
    {
    }

    /**
     * Method called when a key is pressed
     */
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

    /**
     * Method called when a key is released
     */
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

    /**
     * Method called when a button is released
     */
    public void handleButtonReleased(int x, int y, int button)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.enabled)
                widget.onButtonReleased(x, y, button);
        }

        if(selectedWidget != null)
            if(selectedWidget.enabled && selectedWidget.isMouseOver(x, y))
            {
                if(button == 0)
                    actionPerformed(selectedWidget);
            }
    }

    /**
     * Method called when a button is pressed
     */
    public void handleButtonPressed(int x, int y, int button)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.enabled)
            {
                widget.onButtonPressed(x, y, button);
                if(widget.isMouseOver(x, y))
                    selectedWidget = widget;
            }
        }
    }

    /**
     * Returns true if the updating of the game should be paused when this gui is opened (
     */
    public boolean pausesGame()
    {
        return false;
    }

    /**
     * Returns true if mouse needs to be ungrabbed
     */
    public abstract boolean requiresMouse();

    public abstract void init();

    /**
     * Renders this gui on screen
     */
    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        for(GuiWidget widget : widgets)
        {
            widget.render(mx, my, renderEngine);
        }
    }

    /**
     * Draws default background on screen
     */
    public void drawBackground(int mx, int my, RenderEngine renderEngine)
    {
        renderEngine.bindTexture(backgroundTexture, 0);
        drawTexturedRect(renderEngine, 0, 0, OurCraft.getOurCraft().getDisplayWidth(), OurCraft.getOurCraft().getDisplayHeight(), 0, 0, OurCraft.getOurCraft().getDisplayWidth() / backgroundTexture.getWidth(), OurCraft.getOurCraft().getDisplayHeight() / backgroundTexture.getHeight());
    }

    public abstract void update();

    public void handleMouseWheelMovement(int mx, int my, int deltaWheel)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.handleMouseWheelMovement(mx, my, deltaWheel))
            {
                break;
            }
        }
    }
}
