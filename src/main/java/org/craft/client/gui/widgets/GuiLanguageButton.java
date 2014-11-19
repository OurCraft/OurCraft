package org.craft.client.gui.widgets;

import java.io.*;

import org.craft.client.*;
import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.resources.*;

public class GuiLanguageButton extends GuiSmallButton
{
    private TextureAtlas     atlas;
    private float            animCounter;
    private ResourceLocation earthLoc;

    public GuiLanguageButton(int id, int x, int y)
    {
        super(id, x, y, "", null);
        setWidth(40);
        setHeight(40);
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
    public void render(int mx, int my, RenderEngine engine)
    {
        super.render(mx, my, engine);
        if(visible)
        {
            engine.bindLocation(earthLoc);
            TextureRegion icon = atlas.getTiles()[(int) Math.floor(animCounter)][0];
            Gui.drawTexturedRect(engine, getX(), getY(), getWidth(), getHeight(), icon.getMinU(), 0, icon.getMaxU(), 1);
            float speed = 0.075f;
            animCounter += speed;
            if(animCounter >= 17f)
                animCounter = 0.f;
        }
    }

}
