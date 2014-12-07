package org.craft.client.render.fonts;

import java.io.*;

import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.resources.*;

public class BaseFontRenderer extends FontRenderer
{

    public BaseFontRenderer() throws IOException
    {
        super(new TextureAtlas(OpenGLHelper.loadTexture(OurCraft.getOurCraft().getAssetsLoader().getResource(new ResourceLocation("ourcraft", "textures/font.png"))), 16, 16), null);
    }

    @Override
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
}
