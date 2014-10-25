package org.craft.client.gui;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.lwjgl.input.*;

public class GuiSettings extends Gui
{

    public class GuiOptionSlot extends GuiListSlot
    {

        private GameOption option;
        private GuiButton  button;

        public GuiOptionSlot(GameOption option)
        {
            this.option = option;
            String value = Keyboard.getKeyName(Integer.parseInt(option.getValue()));
            button = new GuiButton(0, 0, 0, 200, 40, value, oc.getFontRenderer());
        }

        public void setValue(String v)
        {
            option.setValue(v);
            button.setText(Keyboard.getKeyName(Integer.parseInt(option.getValue())));
        }

        @Override
        public void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner)
        {
            FontRenderer font = oc.getFontRenderer();
            String name = I18n.format("settings." + option.getID());
            font.drawShadowedString(name, 0xFFFFFFFF, x, y + h / 2 - (int) font.getCharHeight('A') / 2, engine);
            button.setLocation(x + w - 220, y + (h / 2 - 20));
            button.render(mx, my, engine);
        }

        public void onButtonPressed(int index, int x, int y, int w, int h, int mx, int my, int button)
        {
            this.button.onButtonPressed(mx, my, button);
        }

        public void onButtonReleased(int index, int x, int y, int w, int h, int mx, int my, int button)
        {
            if(this.button.isPressed() && this.button.isMouseOver(mx, my) && pending == null)
            {
                this.button.setText(">> ? <<");
                pending = this;
            }
            this.button.onButtonReleased(mx, my, button);
        }
    }

    private GuiOptionSlot          pending;
    private Gui                    parent;
    private GuiList<GuiOptionSlot> optionsList;

    public GuiSettings(OurCraft game, Gui parent)
    {
        super(game);
        this.parent = parent;
    }

    public void keyPressed(int id, char c)
    {
        super.keyPressed(id, c);
    }

    public void keyReleased(int id, char c)
    {
        super.keyReleased(id, c);
        if(pending != null)
        {
            pending.setValue("" + id);
            pending = null;
        }
    }

    @Override
    public void init()
    {
        optionsList = new GuiList<GuiOptionSlot>(0, 40, 60, oc.getDisplayWidth() - 80, oc.getDisplayHeight() - 80, 80);
        optionsList.addSlot(new GuiOptionSlot(oc.getGameSettings().jumpKey));
        optionsList.addSlot(new GuiOptionSlot(oc.getGameSettings().forwardKey));
        optionsList.addSlot(new GuiOptionSlot(oc.getGameSettings().backwardsKey));
        optionsList.addSlot(new GuiOptionSlot(oc.getGameSettings().leftKey));
        optionsList.addSlot(new GuiOptionSlot(oc.getGameSettings().rightKey));
        addWidget(optionsList);
        addWidget(new GuiButton(10, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() - 40, 300, 40, I18n.format("menu.back"), getFontRenderer()));
    }

    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 10)
        {
            oc.saveSettings();
            oc.openMenu(parent);
        }
    }

    @Override
    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        if(parent instanceof GuiMainMenu)
            drawBackground(mx, my, renderEngine);
        super.draw(mx, my, renderEngine);

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

}
