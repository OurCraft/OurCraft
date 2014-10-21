package org.craft.client.render.fonts;

import java.awt.*;

import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.resources.*;

public class TrueTypeFontRenderer extends FontRenderer
{

    private static TrueTypeFont font;

    public TrueTypeFontRenderer(String fontName)
    {
        super(createAtlas(font = new TrueTypeFont(new Font("Times New Roman", Font.PLAIN, 16), false)), genChars());
    }

    private static String genChars()
    {
        String s = "";
        for(char c = 0; c <= 256; c++ )
            s += c;
        return s;
    }

    private static TextureAtlas createAtlas(TrueTypeFont ttf)
    {
        TextureMap map = new TextureMap(OurCraft.getOurCraft().getAssetsLoader(), new ResourceLocation(""), true);

        String s = genChars();
        for(char c : s.toCharArray())
            map.generateIcon(ttf.getFontImage(c));
        try
        {
            map.compile();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Texture texture = map.getTexture();
        TextureAtlas atlas = new TextureAtlas(texture, map.getTileWidth(), map.getTileHeight());
        return atlas;
    }

    @Override
    public float getCharWidth(char c)
    {
        return font.getCharWidth(c);
    }

    @Override
    public float getCharHeight(char c)
    {
        return font.getCharHeight(c);
    }

    public double getCharSpacing(char c, char next)
    {
        return 2;
    }

    public void drawString(String text, int color, int xo, int yo, RenderEngine renderEngine)
    {
        super.drawString(text, color, xo, yo, renderEngine);
    }

}
