package org.craft.client.gui;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.resources.*;

public class GuiLanguage extends Gui
{

    public class GuiLangSlot extends GuiListSlot
    {

        private String lang;
        private String langName;

        public GuiLangSlot(String language, String name)
        {
            this.lang = language;
            this.langName = name;
        }

        @Override
        public void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner)
        {
            if(selected)
                drawTexturedRect(engine, x, y, w, h, 0, 0, 1, 1);
            getFontRenderer().drawShadowedString(langName, 0xFFFFFFFF, x + w / 2 - (int) getFontRenderer().getTextWidth(langName) / 2, y + h / 2 - (int) getFontRenderer().getCharHeight('A') / 2, engine);
        }

        public String getLanguage()
        {
            return lang;
        }

    }

    private Gui                  parent;
    private GuiList<GuiLangSlot> langsList;

    private TextureAtlas         atlas;
    private float                animCounter;
    private ResourceLocation     earthLoc;

    public GuiLanguage(OurCraft game, Gui parent)
    {
        super(game);
        this.parent = parent;

        earthLoc = new ResourceLocation("ourcraft:textures/gui/earth_anim.png");
        try
        {
            atlas = new TextureAtlas(OpenGLHelper.loadTexture(OurCraft.getOurCraft().getAssetsLoader().getResource(earthLoc)), 20, 20);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void init()
    {
        langsList = new GuiList<GuiLangSlot>(0, 40, oc.getDisplayHeight() / 8, oc.getDisplayWidth() - 80, oc.getDisplayHeight() - oc.getDisplayHeight() / 8 - 40, 40);
        int i = 0;
        for(Entry<String, HashMap<String, String>> entry : I18n.getAllLanguages().entrySet())
        {
            langsList.addSlot(new GuiLangSlot(entry.getKey(), I18n.getLangName(entry.getKey())));
            if(entry.getKey().equals(I18n.getCurrentLanguageID()))
            {
                langsList.setSelectedIndex(i);
            }
            i++ ;
        }
        addWidget(langsList);
        addWidget(new GuiButton(10, oc.getDisplayWidth() / 2 - 150, oc.getDisplayHeight() - 40, 300, 40, I18n.format("menu.back"), getFontRenderer()));
    }

    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            GuiLangSlot slot = langsList.getSelected();
            if(slot != null)
            {
                int index = langsList.getSelectedIndex();
                String lang = slot.getLanguage();
                I18n.setCurrentLanguage(lang);
                removeAllWidgets();
                init();
                langsList.setSelectedIndex(index);
                oc.getGameSettings().lang.setValue(lang);
                oc.saveSettings();
            }
        }
        else if(widget.getID() == 10)
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

        String s = I18n.format("main.lang");
        getFontRenderer().drawShadowedString(s, 0xFFFFFFFF, (int) (oc.getDisplayWidth() / 2 - getFontRenderer().getTextWidth(s) / 2), (int) (oc.getDisplayHeight() / 16), renderEngine);

        renderEngine.bindLocation(earthLoc);
        TextureRegion icon = atlas.getTiles()[(int) Math.floor(animCounter)][0];
        drawTexturedRect(renderEngine, 0, 0, 80, 80, icon.getMinU(), 0, icon.getMaxU(), 1);

        float speed = 0.075f;
        animCounter += speed;
        if(animCounter >= 17f)
            animCounter = 0.f;
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
