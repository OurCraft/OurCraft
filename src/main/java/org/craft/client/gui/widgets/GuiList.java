package org.craft.client.gui.widgets;

import java.util.*;

import org.craft.client.render.*;

public class GuiList<T extends GuiListSlot> extends GuiWidget
{

    private ArrayList<T> list;
    private int          scroll;
    private int          selectedIndex;
    private int          slotHeight;

    public GuiList(int id, int x, int y, int w, int h, int slotHeight)
    {
        super(id, x, y, w, h);
        this.slotHeight = slotHeight;
        selectedIndex = -1;
        list = new ArrayList<T>();
    }

    public T getSlot(int index)
    {
        return list.get(index);
    }

    public int getSize()
    {
        return list.size();
    }

    public boolean onButtonReleased(int mx, int my, int button)
    {
        if(isMouseOver(mx, my))
        {
            int y = my + scroll - getY();
            int index = y / slotHeight;
            selectedIndex = index;
            if(selectedIndex < 0 || selectedIndex >= getSize())
                selectedIndex = -1;
        }
        return true;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        if(visible)
        {
            for(int index = 0; index < getSize(); index++ )
            {
                T slot = getSlot(index);
                if(slot != null)
                {
                    if(-scroll + index * slotHeight + slotHeight < 0) // Too high
                    {
                        continue;
                    }
                    if(-scroll + index * slotHeight + slotHeight >= getHeight()) // Too low
                    {
                        continue;
                    }
                    slot.render(index, getX(), getY() - scroll + index * slotHeight, getWidth(), slotHeight, mx, my, selectedIndex == index, engine, this);
                }
            }
        }
    }

    public void addSlot(T newSlot)
    {
        list.add(newSlot);
    }

    public T getSelected()
    {
        if(selectedIndex != -1)
            return list.get(selectedIndex);
        return null;
    }

}
