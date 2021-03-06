package org.craft.client.gui;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.glfwGetKeyName;

public class GuiSettings extends Gui
{

    public class GuiOptionSlot<T> extends GuiListSlot
    {

        private GameOption<T> option;
        private GuiWidget     widget;

        public GuiOptionSlot(GameOption<T> option)
        {
            this.option = option;
            if(option.getType() == GameOptionType.INPUT)
            {
                T v = option.getValue();
                String value = glfwGetKeyName((Integer) v, 0);
                widget = new GuiButton(0, 0, 0, 200, 40, value, oc.getFontRenderer());
            }
            else if(option.getType() == GameOptionType.RANGE)
            {
                widget = new GuiSlider(0, 0, 0, 200, 40, 0, 1, oc.getFontRenderer());
                if(option.getValue() instanceof Float)
                    ((GuiSlider) widget).setValue((Float) option.getValue());
            }
            else if(option.getType() == GameOptionType.CHOICE)
            {
                widget = new GuiComboBox(0, 0, 0, 200, 40, oc, fontRenderer);
                String[] values = new String[option.getPossibleValues().length];
                for(int i = 0; i < option.getPossibleValues().length; i++ )
                {
                    values[i] = option.getPossibleValues()[i].toString();
                }
                ((GuiComboBox) widget).setValues(values);
                ((GuiComboBox) widget).setSelected((String) option.getValue());
            }
        }

        public void setValue(T v)
        {
            option.setValue(v);
            if(option.getType() == GameOptionType.INPUT)
            {
                ((GuiButton) widget).setText(glfwGetKeyName((Integer) option.getValue(), 0));
            }
            else if(option.getType() == GameOptionType.INPUT)
            {

            }
        }

        @Override
        public void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner)
        {
            FontRenderer font = oc.getFontRenderer();
            String name = I18n.format("settings." + option.getID());
            font.drawShadowedString(name, 0xFFFFFFFF, x, y + h / 2 - (int) font.getCharHeight('A') / 2, engine);
            widget.setLocation(x + w - 220, y + (h / 2 - 20));
            widget.render(mx, my, engine);
        }

        @Override
        public void onButtonPressed(int index, int x, int y, int w, int h, int mx, int my, int button, GuiList<?> owner)
        {
            this.widget.onButtonPressed(mx, my, button);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onButtonReleased(int index, int x, int y, int w, int h, int mx, int my, int button, GuiList<?> owner)
        {
            if(option.getType() == GameOptionType.INPUT)
            {
                if(((GuiButton) this.widget).isPressed() && this.widget.isMouseOver(mx, my) && pending == null)
                {
                    ((GuiButton) this.widget).setText(">> ? <<");
                    pending = this;
                }
                this.widget.onButtonReleased(mx, my, button);
            }
            else if(option.getType() == GameOptionType.RANGE)
            {
                this.widget.onButtonReleased(mx, my, button);
                option.setValue((T) Float.valueOf(((GuiSlider) widget).getValue()));
            }
            else
            {
                widget.onButtonReleased(mx, my, button);
                if(option.getType() == GameOptionType.CHOICE)
                {
                    option.setValue((T) ((GuiComboBox) widget).getSelected());
                }
            }

            if(option.getCallback() != null)
                option.getCallback().run();
        }

        public boolean onMouseWheelMoved(int index, int x, int y, int w, int h, int mx, int my, int dwheel, GuiList<?> owner)
        {
            return widget.handleMouseWheelMovement(mx, my, dwheel);
        }

        @Override
        public void handleMouseMovement(int index, int x, int y, int w, int h, int mx, int my, int dx, int dy)
        {
            this.widget.handleMouseMovement(mx, my, dx, dy);
        }

        public GuiWidget getWidget()
        {
            return widget;
        }
    }

    private GuiOptionSlot<?>          pending;
    private Gui                       parent;
    private GuiList<GuiOptionSlot<?>> optionsList;

    public GuiSettings(OurCraft game, Gui parent)
    {
        super(game);
        this.parent = parent;
    }

    @Override
    public boolean keyPressed(int id, char c)
    {
        return super.keyPressed(id, c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean keyReleased(int id, char c)
    {
        super.keyReleased(id, c);
        if(pending != null)
        {
            ((GuiOptionSlot<Integer>) pending).setValue(id);
            pending = null;
        }
        return false;
    }

    @Override
    public void init()
    {
        optionsList = new GuiList<GuiOptionSlot<?>>(0, 40, 60, oc.getDisplayWidth() - 80, oc.getDisplayHeight() - 80, 80);
        optionsList.addSlot(new GuiOptionSlot<Integer>(oc.getGameSettings().jumpKey));
        optionsList.addSlot(new GuiOptionSlot<Integer>(oc.getGameSettings().forwardKey));
        optionsList.addSlot(new GuiOptionSlot<Integer>(oc.getGameSettings().backwardsKey));
        optionsList.addSlot(new GuiOptionSlot<Integer>(oc.getGameSettings().leftKey));
        optionsList.addSlot(new GuiOptionSlot<Integer>(oc.getGameSettings().rightKey));
        GuiOptionSlot<Float> sensitivitySlot = new GuiOptionSlot<Float>(oc.getGameSettings().sensitivity);
        ((GuiSlider) sensitivitySlot.getWidget()).setRangeMax(3);
        ((GuiSlider) sensitivitySlot.getWidget()).setValue(OurCraft.getOurCraft().getGameSettings().sensitivity.getValue());
        optionsList.addSlot(sensitivitySlot);
        optionsList.addSlot(new GuiOptionSlot<Float>(oc.getGameSettings().masterVolume));
        optionsList.addSlot(new GuiOptionSlot<Float>(oc.getGameSettings().musicVolume));
        optionsList.addSlot(new GuiOptionSlot<Float>(oc.getGameSettings().soundVolume));
        optionsList.addSlot(new GuiOptionSlot<String>(oc.getGameSettings().palette));
        addWidget(optionsList);
        addWidget(new GuiButton(10, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() - 40, 300, 40, I18n.format("menu.back"), getFontRenderer()));
    }

    @Override
    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 10)
        {
            oc.saveSettings();
            oc.openMenu(parent);
        }
    }

    @Override
    public void render(int mx, int my, RenderEngine renderEngine)
    {
        if(parent instanceof GuiMainMenu)
            drawBackground(mx, my, renderEngine);
        super.render(mx, my, renderEngine);

        String s = I18n.format("main.settings");
        getFontRenderer().drawShadowedString(s, 0xFFFFFFFF, (int) (oc.getDisplayWidth() / 2 - getFontRenderer().getTextWidth(s) / 2), (int) (oc.getDisplayHeight() / 16), renderEngine);
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public boolean pausesGame()
    {
        return true;
    }
}
