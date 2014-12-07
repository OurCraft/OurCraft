package org.craft.client.gui;

import java.io.*;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.modding.*;

public class GuiAddonList extends Gui
{

    private GuiList<GuiListSlot> modList;
    private GuiLabel             titleLabel;
    private GuiPanel             modDataPanel;

    public GuiAddonList(OurCraft game)
    {
        super(game);
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        titleLabel = new GuiLabel(-1, getWidth() / 2, 30, I18n.format("menu.addonlist.title"), getFontRenderer());
        titleLabel.setAlignment(TextAlignment.CENTERED);
        titleLabel.setTextScale(1.5f);
        addWidget(titleLabel);
        modList = new GuiList<GuiListSlot>(0, 10, 40, Math.max(getWidth() / 6, 200), getHeight() - 100, 40);
        modList.setYSpacing(2);

        int maxWidth = 0;
        for(AddonContainer<?> container : oc.getAddonsLoader().getContainersList())
        {
            GuiLabel nameLabel = new GuiLabel(-1, 0, 0, container.getName(), getFontRenderer());
            nameLabel.setTextScale(1.5f);
            GuiLabel subLabel = new GuiLabel(-1, 0, 0, container.getAuthor() + " - " + container.getVersion(), getFontRenderer());
            GuiDualTextSlot slot = new GuiDualTextSlot(nameLabel, subLabel);
            modList.addSlot(slot);
            slot.setData(container);
            if(maxWidth < nameLabel.getWidth())
                maxWidth = nameLabel.getWidth();
            if(maxWidth < subLabel.getWidth())
                maxWidth = subLabel.getWidth();
            nameLabel.update();
        }
        modList.setWidth(Math.max(getWidth() / 6, maxWidth));
        addWidget(modList);

        GuiButton backButton = new GuiButton(1, getWidth() / 2 - 100, getHeight() - 40, 200, 40, I18n.format("menu.back"), getFontRenderer());
        addWidget(backButton);

        if(modDataPanel != null)
            addWidget(modDataPanel);
    }

    @Override
    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            GuiDualTextSlot slot = (GuiDualTextSlot) modList.getSelected();
            if(slot != null)
            {
                AddonContainer<?> container = (AddonContainer<?>) slot.getData();
                if(modDataPanel != null)
                    widgets.remove(modDataPanel);
                modDataPanel = new GuiPanel(modList.getWidth() + 30, 45, getWidth() - modList.getWidth() - 30, modList.getHeight() - 5, oc, getFontRenderer());
                AddonData data = container.getAddonData();
                if(data != null)
                {
                    modDataPanel.addWidget(new GuiLabel(-1, 0, 0, container.getName(), getFontRenderer()));
                    int descY = 20;
                    if(data.getLogoPath() != null)
                    {
                        try
                        {
                            GuiImage image = new GuiImage(-1, 0, 24, data.getLogoPath());
                            descY += image.getHeight();
                            modDataPanel.addWidget(image);
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(!data.getDescription().isEmpty())
                    {
                        GuiLabel descLabel = new GuiLabel(-1, 0, descY, "Description: " + data.getDescription(), getFontRenderer());
                        modDataPanel.addWidget(descLabel);
                    }
                }
                else
                {
                    GuiLabel unavailableLabel = new GuiLabel(-1, modDataPanel.getWidth() / 2, modDataPanel.getHeight() / 2, "UNAVAILABLE INFOS", getFontRenderer());
                    unavailableLabel.setColor(0xFFFF2020);
                    unavailableLabel.setAlignment(TextAlignment.CENTERED);
                    unavailableLabel.setTextScale(2f);
                    modDataPanel.addWidget(unavailableLabel);
                }
                addWidget(modDataPanel);
            }
        }
        else if(widget.getID() == 1)
        {
            oc.openMenu(new GuiMainMenu(oc));
        }
    }

    @Override
    public void render(int mx, int my, RenderEngine renderEngine)
    {
        drawBackground(mx, my, renderEngine);
        super.render(mx, my, renderEngine);
    }

}
