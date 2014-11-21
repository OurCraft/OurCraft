package org.craft.client.gui;

import java.io.*;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.modding.events.gui.*;
import org.craft.resources.*;

public abstract class Gui extends GuiPanel
{

    public static ResourceLocation widgetsTexture = new ResourceLocation("ourcraft", "textures/gui/widgets.png");
    private static OpenGLBuffer    buffer;
    private static Texture         backgroundTexture;

    public Gui(OurCraft game)
    {
        super(0, 0, game.getDisplayWidth(), game.getDisplayHeight(), game, game.getFontRenderer());
        if(backgroundTexture == null)
        {
            try
            {
                backgroundTexture = OpenGLHelper.loadTexture(oc.getAssetsLoader().getResource(new ResourceLocation("ourcraft", "textures/gui/background.png")));
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
    }

    private static Vector3    bottomLeftCornerPos    = Vector3.get(0, 0, 0);
    private static Vector3    bottomRightCornerPos   = Vector3.get(0, 0, 0);
    private static Vector3    topLeftCornerPos       = Vector3.get(0, 0, 0);
    private static Vector3    topRightCornerPos      = Vector3.get(0, 0, 0);

    private static Vector2    bottomLeftCornerUV     = Vector2.get(0, 0);
    private static Vector2    bottomRightCornerUV    = Vector2.get(0, 0);
    private static Vector2    topLeftCornerUV        = Vector2.get(0, 0);
    private static Vector2    topRightCornerUV       = Vector2.get(0, 0);

    private static Quaternion bottomLeftCornerColor  = Quaternion.get(1, 1, 1, 1);
    private static Quaternion bottomRightCornerColor = Quaternion.get(1, 1, 1, 1);
    private static Quaternion topLeftCornerColor     = Quaternion.get(1, 1, 1, 1);
    private static Quaternion topRightCornerColor    = Quaternion.get(1, 1, 1, 1);

    /**
     * Draws textured rect at given coordinates
     */
    public static void drawTexturedRect(RenderEngine engine, int x, int y, int w, int h, float minU, float minV, float maxU, float maxV)
    {
        bottomLeftCornerColor.set(1, 1, 1, 1);
        bottomRightCornerColor.set(1, 1, 1, 1);
        topLeftCornerColor.set(1, 1, 1, 1);
        topRightCornerColor.set(1, 1, 1, 1);
        drawRect(engine, x, y, w, h, minU, minV, maxU, maxV);
    }

    private static void drawRect(RenderEngine engine, int x, int y, int w, int h, float minU, float minV, float maxU, float maxV)
    {
        if(buffer == null)
        {
            buffer = new OpenGLBuffer();
            buffer.addVertex(Vertex.get(bottomLeftCornerPos, bottomLeftCornerUV, bottomLeftCornerColor));
            buffer.addVertex(Vertex.get(bottomRightCornerPos, bottomRightCornerUV, bottomRightCornerColor));
            buffer.addVertex(Vertex.get(topLeftCornerPos, topLeftCornerUV, topLeftCornerColor));
            buffer.addVertex(Vertex.get(topRightCornerPos, topRightCornerUV, topRightCornerColor));
            buffer.addIndex(0);
            buffer.addIndex(1);
            buffer.addIndex(2);

            buffer.addIndex(2);
            buffer.addIndex(3);
            buffer.addIndex(0);
        }
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

    /**
     * Creates this gui (adds buttons'n'stuff)
     */
    public abstract void init();

    /**
     * Draws default background on screen
     */
    public void drawBackground(int mx, int my, RenderEngine renderEngine)
    {
        renderEngine.bindTexture(backgroundTexture, 0);
        drawTexturedRect(renderEngine, 0, 0, oc.getDisplayWidth(), oc.getDisplayHeight(), 0, 0, (float) oc.getDisplayWidth() / (float) backgroundTexture.getWidth(), (float) oc.getDisplayHeight() / (float) backgroundTexture.getHeight());
    }

    /**
     * Updates this gui
     */
    public void update()
    {

    }

    /**
     * Clears widget list
     */
    public void build()
    {
        widgets.clear();
        if(!OurCraft.getOurCraft().getEventBus().fireEvent(new GuiBuildingEvent.Pre(OurCraft.getOurCraft(), this)))
        {
            init();
            OurCraft.getOurCraft().getEventBus().fireEvent(new GuiBuildingEvent.Post(OurCraft.getOurCraft(), this));
        }
    }

    public static void drawColoredRect(RenderEngine engine, int x, int y, int w, int h, int color)
    {
        engine.bindTexture(5, 0);
        float a = (float) (color >> 24 & 0xFF) / 255f;
        float r = (float) (color >> 16 & 0xFF) / 255f;
        float g = (float) (color >> 8 & 0xFF) / 255f;
        float b = (float) (color & 0xFF) / 255f;
        bottomLeftCornerColor.set(r, g, b, a);
        bottomRightCornerColor.set(r, g, b, a);
        topLeftCornerColor.set(r, g, b, a);
        topRightCornerColor.set(r, g, b, a);
        drawRect(engine, x, y, w, h, 0, 0, 1, 1);
        engine.bindLocation(widgetsTexture);
    }
}
