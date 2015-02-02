package org.craft.client.gui.widgets;

import org.craft.client.*;
import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.client.render.texture.TextureIcon;
import org.craft.resources.*;

public class GuiPopupElement extends GuiListSlot
{

    public  int              w;
    public  int              h;
    private String           txt;
    private FontRenderer     font;
    private int              id;
    private boolean          enabled;
    private boolean          darkBackground;
    private TextureIcon      icon;
    private ResourceLocation iconLoc;

    public GuiPopupElement(int id, String txt, FontRenderer fontRenderer)
    {
        darkBackground = true;
        enabled = true;
        this.id = id;
        this.txt = txt;
        this.font = fontRenderer;
        this.w = (int) fontRenderer.getTextWidth(txt) + 4 + 34;
        this.h = (int) fontRenderer.getCharHeight('A') + 4;
        if(h < 34)
            h = 34;

        switch((int) (Math.random() * 5.))
        {
            case 0:
                icon = OurCraft.getOurCraft().getRenderEngine().blocksAndItemsMap.get("blocks/dirt.png");
                break;
            case 1:
                icon = OurCraft.getOurCraft().getRenderEngine().blocksAndItemsMap.get("blocks/stone.png");
                break;
            case 2:
                icon = OurCraft.getOurCraft().getRenderEngine().blocksAndItemsMap.get("blocks/log_side.png");
                break;
            case 3:
                icon = OurCraft.getOurCraft().getRenderEngine().blocksAndItemsMap.get("blocks/grass_side.png");
                break;
            case 4:
                icon = OurCraft.getOurCraft().getRenderEngine().blocksAndItemsMap.get("blocks/rose.png");
                break;
        }
        setIcon(OurCraft.getOurCraft().getRenderEngine().blocksAndItemsMapLocation, icon);
    }

    public void setIcon(ResourceLocation loc, TextureIcon icon)
    {
        this.iconLoc = loc;
        this.icon = icon;
    }

    @Override
    public void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner)
    {
        int background = 0xFF666666;
        int foreground = 0xFFA9A9A9;
        if(darkBackground)
        {
            background = 0xFFA9A9A9;
            foreground = 0xFF666666;
        }
        Gui.drawColoredRect(engine, x, y, w, h, background);
        Gui.drawColoredRect(engine, x + 1, y + 0, w - 2, h - 0, foreground);

        Gui.drawColoredRect(engine, x + 1 + 34, y + h - 1, w - 2 - 32, 1, 0xFFD0D0D0);

        Gui.drawColoredRect(engine, x + 33, y + 1, 1, h - 1, 0xFFD0D0D0);
        Gui.drawColoredRect(engine, x + 34, y + 1, 1, h - 1, background);
        int color = 0xFFFFFFFF;
        if(!enabled)
        {
            color = 0xFF707070;
        }
        else if(isMouseOver(mx, my, x, y, w, h))
        {
            color = 0xFFFFF544;
        }
        font.drawShadowedString(txt, color, x + 2 + 34, y + h / 2 - (int) font.getCharHeight('A') / 2 + 2, engine);
        if(iconLoc != null && icon != null)
        {
            engine.bindLocation(iconLoc);
            Gui.drawTexturedRect(engine, x + 1, y, 32, 32, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV());
        }
    }

    @Override
    public void onButtonReleased(int index, int x, int y, int w, int h, int mx, int my, int button, GuiList<?> owner)
    {
        super.onButtonReleased(index, x, y, w, h, mx, my, button, owner);
        if(owner instanceof GuiPopupMenu)
            ((GuiPopupMenu) owner).onClicked(this);
    }

    public int getWidth()
    {
        return w;
    }

    public int getHeight()
    {
        return h;
    }

    public String getText()
    {
        return txt;
    }

    public int getID()
    {
        return id;
    }

}
