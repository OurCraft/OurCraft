package org.craft.client.gui;

import java.io.*;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.utils.*;

public class GuiDeleteWorld extends Gui
{

    private File   saveFolder;
    private File[] worldFolders;
    private String folderName;

    public GuiDeleteWorld(FontRenderer fontRenderer, File saveFolder, File[] worldFolders, String worldFolderName)
    {
        super(fontRenderer);
        this.saveFolder = saveFolder;
        this.worldFolders = worldFolders;
        this.folderName = worldFolderName;
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        String title = I18n.format("menu.deleteworld.title");
        addWidget(new GuiLabel(-1, OurCraft.getOurCraft().getDisplayWidth() / 2 + 10 - (int) getFontRenderer().getTextLength(title) / 2, OurCraft.getOurCraft().getDisplayHeight() / 2 - 90, title, getFontRenderer()));
        GuiButton back = new GuiButton(1, OurCraft.getOurCraft().getDisplayWidth() / 2 + 10, OurCraft.getOurCraft().getDisplayHeight() / 2 + 40, 150, 40, I18n.format("menu.back"), getFontRenderer());
        addWidget(back);

        GuiButton confirm = new GuiButton(0, OurCraft.getOurCraft().getDisplayWidth() / 2 - 160, OurCraft.getOurCraft().getDisplayHeight() / 2 + 40, 150, 40, I18n.format("menu.deleteworld.delete"), getFontRenderer());
        addWidget(confirm);
    }

    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            File file = new File(saveFolder, folderName);
            SystemUtils.deleteRecursivly(file);
            OurCraft.getOurCraft().openMenu(new GuiSelectWorld(getFontRenderer(), saveFolder, saveFolder.listFiles()));
        }
        else if(widget.getID() == 1)
        {
            OurCraft.getOurCraft().openMenu(new GuiSelectWorld(getFontRenderer(), saveFolder, worldFolders));
        }
    }

    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        drawBackground(mx, my, renderEngine);
        super.draw(mx, my, renderEngine);

        String desc0 = I18n.format("menu.deleteworld.desc0");
        String desc1 = I18n.format("menu.deleteworld.desc1");
        getFontRenderer().drawShadowedString(desc0, 0xFFFFFFFF, OurCraft.getOurCraft().getDisplayWidth() / 2 + 10 - (int) getFontRenderer().getTextLength(desc0) / 2, OurCraft.getOurCraft().getDisplayHeight() / 2 - 25, renderEngine);
        getFontRenderer().drawShadowedString(desc1, 0xFFFFFFFF, OurCraft.getOurCraft().getDisplayWidth() / 2 + 10 - (int) getFontRenderer().getTextLength(desc1) / 2, OurCraft.getOurCraft().getDisplayHeight() / 2 - 5, renderEngine);
    }

    @Override
    public void update()
    {

    }

}
