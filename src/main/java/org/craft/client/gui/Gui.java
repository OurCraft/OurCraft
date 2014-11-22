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
     * Clears widget list
     */
    public void build()
    {
        widgets.clear();
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

    public void buildMenu(GuiPopupMenu popupMenu)
    {
    }

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

    public void update()
    {
        popupMenu.update();
        super.update();
    }

    public void onPopupMenuCliked(GuiPopupElement clicked)
    {
        hidePopupMenu();
    }
}
