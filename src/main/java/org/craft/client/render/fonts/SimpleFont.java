package org.craft.client.render.fonts;

import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.resources.*;

public class SimpleFont extends Font
{

    private static SimpleFont instance;

    private SimpleFont() throws Exception
    {
        super(new TextureAtlas(OpenGLHelper.loadTexture(OurCraft.getOurCraft().getBaseLoader().getResource(new ResourceLocation("ourcraft", "textures/font.png"))), 16, 16), null);
    }

    public double getCharSpacing(char c, char next)
    {
        return -8;
    }

    @Override
    public float getCharWidth(char c)
    {
        return 16;
    }

    @Override
    public float getCharHeight(char c)
    {
        return 16;
    }

    public static SimpleFont getInstance()
    {
        if(instance == null)
        {
            try
            {
                instance = new SimpleFont();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
