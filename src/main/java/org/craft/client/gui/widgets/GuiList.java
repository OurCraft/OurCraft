package org.craft.client.gui.widgets;

import java.util.*;

import org.craft.client.gui.*;
import org.craft.client.render.*;

public class GuiList<T extends GuiListSlot> extends GuiWidget
{

    private ArrayList<T> list;
    private int          scroll;
    private int          selectedIndex;
    private int          slotHeight;
    private int          ySpacing;

    public GuiList(int id, int x, int y, int w, int h, int slotHeight)
    {
        super(id, x, y, w, h);
        this.slotHeight = slotHeight;
        selectedIndex = -1;
        list = new ArrayList<T>();
    }

    public int getYSpacing()
    {
        return ySpacing;
    }

    public void setYSpacing(int ySpacing)
    {
        this.ySpacing = ySpacing;
    }

    /**
     * Returns a slot at given index or null if index is out of bounds 
     */
    public T getSlot(int index)
    {
        return list.get(index);
    }

    /**
     * Returns the number of slots
     */
    public int getSize()
    {
        return list.size();
    }

    public boolean onButtonReleased(int mx, int my, int button)
    {
        if(isMouseOver(mx, my))
        {
            int y = my + scroll - getY();
            int index = y / (slotHeight + ySpacing);
            selectedIndex = index;
            if(selectedIndex < 0 || selectedIndex >= getSize())
                selectedIndex = -1;
            else
            {
                getSlot(selectedIndex).onButtonReleased(selectedIndex, getX(), getY() - scroll + selectedIndex * slotHeight + selectedIndex * ySpacing, getWidth(), slotHeight, mx, my, button, this);
                return true;
            }
        }
        return false;
    }

    public boolean onButtonPressed(int mx, int my, int button)
    {
        if(isMouseOver(mx, my))
        {
            int y = my + scroll - getY();
            int index = y / (slotHeight + ySpacing);
            if(index < 0 || index >= getSize())
                index = -1;
            else
                getSlot(index).onButtonPressed(index, getX(), getY() - scroll + index * slotHeight + selectedIndex * ySpacing, getWidth(), slotHeight, mx, my, button, this);
        }
        return true;
    }

    public boolean handleMouseMovement(int mx, int my, int dx, int dy)
    {
        if(isMouseOver(mx, my))
        {
            int y = my + scroll - getY();
            int index = y / slotHeight;
            if(index < 0 || index >= getSize())
                index = -1;
            else
                getSlot(index).handleMouseMovement(index, getX(), getY() - scroll + index * slotHeight + selectedIndex * ySpacing, getWidth(), slotHeight, mx, my, dx, dy);
        }
        return true;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        if(visible)
        {
            int start = (scroll) / slotHeight;
            int end = (scroll + getHeight()) / slotHeight;
            if(start < 0)
                start = 0;
            if(end > getSize())
                end = getSize();
            for(int index = start; index < end; index++ )
            {
                T slot = getSlot(index);
                if(slot != null)
                {
                    slot.render(index, getX(), getY() - scroll + index * slotHeight + index * ySpacing, getWidth(), slotHeight, mx, my, selectedIndex == index, engine, this);
                }
            }

            engine.bindLocation(Gui.widgetsTexture);
            float maxHeight = getHeight() - slotHeight;
            Gui.drawTexturedRect(engine, getX() + getWidth(), getY(), 10, (int) maxHeight, 0, 60f / 256f, 10f / 256f, 70f / 256f);
            float startf = (float) scroll / (float) slotHeight;
            float endf = ((float) scroll + (float) getHeight()) / (float) slotHeight;
            if(startf < 0.f)
                startf = 0f;
            if(endf > getSize())
                endf = getSize();
            Gui.drawTexturedRect(engine, getX() + getWidth(),
                    getY() + (int) (startf / (float) getSize() * maxHeight), 10,
                    (int) ((endf - startf) / (float) getSize() * maxHeight), 10f / 256f, 60f / 256f, 20f / 256f, 70f / 256f);
        }
    }

    /**
     * Adds a new slot to this list
     */
    public void addSlot(T newSlot)
    {
        list.add(newSlot);
    }

    /**
     * Returns currently selected slot or null if none selected
     */
    public T getSelected()
    {
        if(selectedIndex != -1)
            return list.get(selectedIndex);
        return null;
    }

    public boolean handleMouseWheelMovement(int mx, int my, int deltaWheel)
    {
        if(isMouseOver(mx, my))
        {
            scroll += -(Math.signum(deltaWheel) * 15);

            if(-scroll + getSize() * slotHeight + getSize() * ySpacing + slotHeight <= getHeight())
            {
                /* -scroll + getSize() * slotHeight + slotHeight <= getHeight()
                * getSize() * slotHeight + slotHeight <= getHeight()+scroll
                * getSize() * slotHeight + slotHeight - getHeight() <= scroll
                * scroll >= getSize() * slotHeight + slotHeight - getHeight()
                */
                scroll = getSize() * slotHeight + getSize() * ySpacing + slotHeight - getHeight();
            }
            if(scroll < 0)
            {
                scroll = 0;
            }
            return true;
        }
        return false;
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
    }
}
