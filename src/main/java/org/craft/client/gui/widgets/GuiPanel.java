package org.craft.client.gui.widgets;

import java.util.*;

import com.google.common.collect.*;

import org.craft.client.*;
import org.craft.client.gui.layout.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.modding.events.gui.*;

public class GuiPanel extends GuiWidget
{
    protected List<GuiWidget> widgets;
    protected GuiWidget       selectedWidget;
    protected OurCraft        oc;
    protected FontRenderer    fontRenderer;
    protected boolean         forceDrawAll;
    private IGuiLayout        layout;

    public GuiPanel(int x, int y, int w, int h, OurCraft oc, FontRenderer fontRenderer)
    {
        this(-1, x, y, w, h, oc, fontRenderer);
    }

    public GuiPanel(int id, int x, int y, int w, int h, OurCraft oc, FontRenderer fontRenderer)
    {
        super(id, x, y, w, h);
        layout = new GuiAbsoluteLayout();
        this.fontRenderer = fontRenderer;
        widgets = Lists.newArrayList();
        this.oc = oc;
    }

    public void setLocation(int x, int y)
    {
        int dx = getX() - x;
        int dy = getY() - y;
        for(GuiWidget widget : widgets)
        {
            widget.setLocation(widget.getX() - dx, widget.getY() - dy);
        }
        super.setLocation(x, y);
    }

    public IGuiLayout getLayout()
    {
        return layout;
    }

    public void setLayout(IGuiLayout layout)
    {
        this.layout = layout;
    }

    public void addWidget(GuiWidget widget)
    {
        layout.onAdd(widget, this);
        widgets.add(widget);
    }

    public void pack()
    {
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        for(GuiWidget widget : widgets)
        {
            int relMinX = widget.getX() - getX();
            int relMinY = widget.getY() - getY();

            int relMaxX = (widget.getX() + widget.getWidth()) - getX();
            int relMaxY = (widget.getY() + widget.getHeight()) - getY();

            if(relMinX < minX)
                minX = relMinX;
            if(relMinY < minY)
                minY = relMinY;
            if(relMaxX > maxX)
                maxX = relMaxX;
            if(relMaxY > maxY)
                maxY = relMaxY;
        }

        this.setWidth((int) (maxX - minX));
        this.setHeight((int) (maxY - minY));
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.getX() + widget.getWidth() >= getX() && widget.getX() <= getX() + getWidth()
                    && widget.getY() + widget.getHeight() >= getY() && widget.getY() <= getY() + getHeight() || forceDrawAll)
                widget.render(mx, my, engine);
        }
    }

    public void actionPerformed(GuiWidget widget)
    {
    }

    /**
     * Method called when a key is pressed
     */
    @Override
    public boolean keyPressed(int id, char c)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.keyPressed(id, c))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Method called when a key is released
     */
    @Override
    public boolean keyReleased(int id, char c)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.keyReleased(id, c))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onButtonReleased(int x, int y, int button)
    {
        boolean result = false;
        for(GuiWidget widget : widgets)
        {
            if(widget.enabled)
                if(widget.onButtonReleased(x, y, button))
                    result = true;
        }

        if(selectedWidget != null)
            if(selectedWidget.enabled && selectedWidget.isMouseOver(x, y))
            {
                if(button == 0)
                {
                    if(!oc.getEventBus().fireEvent(new GuiActionPerformedEvent(oc, this, selectedWidget)))
                        actionPerformed(selectedWidget);
                }
            }
        return result;
    }

    @Override
    public boolean onButtonPressed(int x, int y, int button)
    {
        boolean result = false;
        for(GuiWidget widget : widgets)
        {
            if(widget.enabled)
            {
                if(widget.isMouseOver(x, y))
                    selectedWidget = widget;
                if(widget.onButtonPressed(x, y, button))
                    result = true;
            }
        }
        return result;
    }

    public List<GuiWidget> getAllWidgets()
    {
        return widgets;
    }

    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    /**
     * Handles a 'mouse moved event'
     * @param mx : The new position of mouse on X axis
     * @param my : The new position of mouse on Y axis
     * @param dx : The movement of mouse on X axis
     * @param dy : The movement of mouse on Y axis
     */
    @Override
    public boolean handleMouseMovement(int mx, int my, int dx, int dy)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.handleMouseMovement(mx, my, dx, dy))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles a 'mouse wheel moved' event
     */
    @Override
    public boolean handleMouseWheelMovement(int mx, int my, int deltaWheel)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.handleMouseWheelMovement(mx, my, deltaWheel))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates this panel and its children
     */
    @Override
    public void update()
    {
        for(GuiWidget widget : widgets)
        {
            widget.update();
        }
    }
}
