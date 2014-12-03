package org.craft.client.gui.widgets;

import java.util.*;

import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.modding.events.gui.*;

import com.google.common.collect.Lists;

public class GuiPanel extends GuiWidget
{
    protected List<GuiWidget> widgets;
    protected GuiWidget            selectedWidget;
    protected OurCraft             oc;
    protected FontRenderer         fontRenderer;
    protected boolean              forceDrawAll;

    public GuiPanel(int x, int y, int w, int h, OurCraft oc, FontRenderer fontRenderer)
    {
        this(-1, x, y, w, h, oc, fontRenderer);
    }

    public GuiPanel(int id, int x, int y, int w, int h, OurCraft oc, FontRenderer fontRenderer)
    {
        super(id, x, y, w, h);
        this.fontRenderer = fontRenderer;
        widgets = Lists.newArrayList();
        this.oc = oc;
    }

    public void addWidget(GuiWidget widget)
    {
        widget.setLocation(widget.getX() + getX(), widget.getY() + getY());
        widgets.add(widget);
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        for(GuiWidget widget : widgets)
        {
            if(widget.getX() >= getX() && widget.getX() + widget.getWidth() <= getX() + getWidth()
                    && widget.getY() >= getY() && widget.getY() + widget.getHeight() <= getY() + getHeight() || forceDrawAll)
                widget.render(mx, my, engine);
        }
    }

    public void actionPerformed(GuiWidget widget)
    {
    }

    /**
     * Method called when a key is pressed
     */
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
    public void update()
    {
        for(GuiWidget widget : widgets)
        {
            widget.update();
        }
    }
}
