package org.craft.client.gui.widgets;

import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;

public class GuiDualTextSlot extends GuiListSlot
{

    private GuiLabel mainLabel;
    private GuiLabel subLabel;
    private Object   data;

    public GuiDualTextSlot(String mainText, String subText, FontRenderer font)
    {
        this(new GuiLabel(-1, 0, 0, mainText, font), prepareSubLabel(subText, font));
    }

    private static GuiLabel prepareSubLabel(String subText, FontRenderer font)
    {
        GuiLabel subLabel = new GuiLabel(-1, 0, 0, subText, font);
        subLabel.setTextScale(0.75f);
        return subLabel;
    }

    public GuiDualTextSlot(GuiLabel mainLabel, GuiLabel subLabel)
    {
        this.mainLabel = mainLabel;
        this.subLabel = subLabel;
    }

    @Override
    public void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner)
    {
        if(isMouseOver(mx, my, x, y, w, h))
        {
            mainLabel.setColor(0xFFFFF544);
            subLabel.setColor(0xFFFFF544);
        }
        else
        {
            mainLabel.setColor(0xFFFFFFFF);
            subLabel.setColor(0xFFFFFFFF);
        }
        TextAlignment labelAlign = mainLabel.getAlignment();
        switch(labelAlign)
        {
            case LEFT:
                mainLabel.setLocation(x, y - 10);
                subLabel.setLocation(x, y + 10);
                break;

            case CENTERED:
                mainLabel.setLocation(x + w / 2, y + h / 2 - 10);
                subLabel.setLocation(x + w / 2, y + h / 2 + 10);
                break;

            case RIGHT:
                mainLabel.setLocation(x + w, y + h - 10);
                subLabel.setLocation(x + w / 2, y + h / 2 + 10);
                break;
        }
        mainLabel.render(mx, my, engine);
        subLabel.render(mx, my, engine);
    }

    /**
     * Stores an user-provided object to be retrieved from the slot
     * @param userData Object provided by the user to be saved in this slot
     * @see #getData()
     */
    public void setData(Object userData)
    {
        this.data = userData;
    }

    /**
     * Gets the user-provided object saved in this slot. 
     * @return Object provided by the user saved in this slot.
     * @see #setData(Object)
     */
    public Object getData()
    {
        return data;
    }

}
