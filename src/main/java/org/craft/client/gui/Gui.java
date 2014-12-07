package org.craft.client.gui;

import static org.lwjgl.opengl.GL11.*;

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
    private GuiPopupMenu           popupMenu;

    public Gui(OurCraft game)
    {
        super(0, 0, game.getDisplayWidth(), game.getDisplayHeight(), game, game.getFontRenderer());
        popupMenu = new GuiPopupMenu(this);
        popupMenu.visible = false;
        forceDrawAll = true;
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
        initBufferIfNeeded();
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

    public static void drawBezierCurve(RenderEngine engine, int color, int segments, float lineWidth, Vector2... points)
    {
        double[] x = new double[points.length];
        double[] y = new double[points.length];
        double[] z = new double[points.length];
        for(int i = 0; i < x.length; i++ )
        {
            Vector2 point = points[i];
            x[i] = point.getX();
            y[i] = point.getY();
            z[i] = 0;
        }
        CasteljauAlgorithm algo = new CasteljauAlgorithm(x, y, z, x.length);
        double step = 1.0 / segments;
        Vector2 previous = null;
        for(double t = 0; t <= 1.0; t += step)
        {
            double[] values = algo.getXYZvalues(t);
            Vector2 current = Vector2.get((float) values[0], (float) values[1]);
            if(previous != null)
            {
                drawColoredLine(engine, (int) current.getX(), (int) current.getY(), (int) previous.getX(), (int) previous.getY(), color, lineWidth);
                //                previous.dispose();
            }
            previous = current;
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
     * Clears widget list
     */
    public void build()
    {
        widgets.clear();
        setWidth(oc.getDisplayWidth());
        setHeight(oc.getDisplayHeight());
        if(!OurCraft.getOurCraft().getEventBus().fireEvent(new GuiBuildingEvent.Pre(OurCraft.getOurCraft(), this)))
        {
            init();
            popupMenu.clear();
            if(!OurCraft.getOurCraft().getEventBus().fireEvent(new GuiBuildingEvent.PopupMenu.Pre(OurCraft.getOurCraft(), this, popupMenu)))
            {
                buildMenu(popupMenu);
                OurCraft.getOurCraft().getEventBus().fireEvent(new GuiBuildingEvent.PopupMenu.Post(OurCraft.getOurCraft(), this, popupMenu));
            }
            OurCraft.getOurCraft().getEventBus().fireEvent(new GuiBuildingEvent.Post(OurCraft.getOurCraft(), this));
        }
    }

    public GuiPopupMenu getPopupMenu()
    {
        return popupMenu;
    }

    public static void drawColoredLine(RenderEngine engine, int x, int y, int x2, int y2, int color, float lineWidth)
    {
        engine.bindTexture(Texture.empty);
        float a = (float) (color >> 24 & 0xFF) / 255f;
        float r = (float) (color >> 16 & 0xFF) / 255f;
        float g = (float) (color >> 8 & 0xFF) / 255f;
        float b = (float) (color & 0xFF) / 255f;
        bottomLeftCornerColor.set(r, g, b, a);
        bottomRightCornerColor.set(r, g, b, a);
        topLeftCornerColor.set(r, g, b, a);
        topRightCornerColor.set(r, g, b, a);
        drawLine(engine, x, y, x2, y2, 0, 0, 1, 1, lineWidth);
        engine.bindLocation(widgetsTexture);
    }

    private static void initBufferIfNeeded()
    {
        bottomLeftCornerPos.setDisposable(false);
        bottomLeftCornerUV.setDisposable(false);
        bottomRightCornerPos.setDisposable(false);
        bottomRightCornerUV.setDisposable(false);
        topLeftCornerPos.setDisposable(false);
        topLeftCornerUV.setDisposable(false);
        topRightCornerPos.setDisposable(false);
        topRightCornerUV.setDisposable(false);
        if(buffer == null)
        {
            buffer = new OpenGLBuffer();
            Vertex a = Vertex.get(bottomLeftCornerPos, bottomLeftCornerUV, bottomLeftCornerColor);
            a.setDisposable(false);
            buffer.addVertex(a);
            Vertex b = Vertex.get(bottomRightCornerPos, bottomRightCornerUV, bottomRightCornerColor);
            b.setDisposable(false);
            buffer.addVertex(b);
            Vertex c = Vertex.get(topLeftCornerPos, topLeftCornerUV, topLeftCornerColor);
            c.setDisposable(false);
            buffer.addVertex(c);
            Vertex d = Vertex.get(topRightCornerPos, topRightCornerUV, topRightCornerColor);
            d.setDisposable(false);
            buffer.addVertex(d);
            buffer.addIndex(0);
            buffer.addIndex(1);
            buffer.addIndex(2);

            buffer.addIndex(2);
            buffer.addIndex(3);
            buffer.addIndex(0);
        }
    }

    public static void drawLine(RenderEngine engine, int x, int y, int x2, int y2, float minU, float minV, float maxU, float maxV, float lineWidth)
    {
        initBufferIfNeeded();
        bottomLeftCornerPos.set(x, y, 0);
        bottomRightCornerPos.set(x2, y2, 0);
        topLeftCornerPos.set(x2, y2, 0);
        topRightCornerPos.set(x2, y2, 0);
        bottomLeftCornerUV.set(minU, minV);
        bottomRightCornerUV.set(maxU, minV);
        topLeftCornerUV.set(maxU, maxV);
        topRightCornerUV.set(minU, maxV);
        buffer.upload();
        glLineWidth(lineWidth);
        engine.renderBuffer(buffer, GL_LINES);
        glLineWidth(1);
    }

    public static void drawColoredRect(RenderEngine engine, int x, int y, int w, int h, int color)
    {
        engine.bindTexture(Texture.empty);
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

    public void buildMenu(GuiPopupMenu popupMenu)
    {
    }

    @Override
    public boolean onButtonReleased(int x, int y, int button)
    {
        boolean result = super.onButtonReleased(x, y, button);
        if(!result && button == 1)
        {
            showPopupMenu(x, y);
            return true;
        }
        else if(popupMenu.visible)
        {
            if(!popupMenu.onButtonReleased(x, y, button))
            {
                hidePopupMenu();
            }
        }
        return result;
    }

    private void hidePopupMenu()
    {
        popupMenu.visible = false;
    }

    private void showPopupMenu(int x, int y)
    {
        popupMenu.visible = true;
        popupMenu.pack();
        popupMenu.setLocation(x, y);
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        super.render(mx, my, engine);
        if(popupMenu.visible)
            popupMenu.render(mx, my, engine);
    }

    @Override
    public boolean keyPressed(int id, char c)
    {
        boolean result = super.keyPressed(id, c);
        if(!result && popupMenu.visible)
        {
            return popupMenu.keyPressed(id, c);
        }
        return result;
    }

    @Override
    public boolean keyReleased(int id, char c)
    {
        boolean result = super.keyReleased(id, c);
        if(!result && popupMenu.visible)
        {
            return popupMenu.keyReleased(id, c);
        }
        return result;
    }

    @Override
    public boolean onButtonPressed(int x, int y, int button)
    {
        boolean result = super.onButtonPressed(x, y, button);
        if(!result && popupMenu.visible)
        {
            return popupMenu.onButtonPressed(x, y, button);
        }
        return result;
    }

    @Override
    public boolean handleMouseMovement(int mx, int my, int dx, int dy)
    {
        boolean result = super.handleMouseMovement(mx, my, dx, dy);
        if(!result && popupMenu.visible)
        {
            return popupMenu.handleMouseMovement(mx, my, dx, dy);
        }
        return result;
    }

    @Override
    public boolean handleMouseWheelMovement(int mx, int my, int deltaWheel)
    {
        boolean result = super.handleMouseWheelMovement(mx, my, deltaWheel);
        if(!result && popupMenu.visible)
        {
            return popupMenu.handleMouseWheelMovement(mx, my, deltaWheel);
        }
        return result;
    }

    @Override
    public void update()
    {
        popupMenu.update();
        super.update();
    }

    public void onPopupMenuClicked(GuiPopupElement clicked)
    {
        hidePopupMenu();
    }
}
