package org.craft.client.render.fonts;

import java.awt.*;

import org.craft.client.*;
import org.craft.client.render.*;
import org.craft.resources.*;

public class TrueTypeFontRenderer extends FontRenderer
{

    private TrueTypeFont font;

    public TrueTypeFontRenderer(String fontName)
    {
        super(null, null);
        supportedChars = "";
        for(char c = 0; c <= 256; c++ )
            supportedChars += c;
        this.font = new TrueTypeFont(new Font(fontName, Font.PLAIN, 16), false, supportedChars.toCharArray());
        TextureMap map = new TextureMap(OurCraft.getOurCraft().getAssetsLoader(), new ResourceLocation("Font_" + fontName), true);

        for(char c : supportedChars.toCharArray())
            map.generateIcon(font.getFontImage(c));
        try
        {
            map.compile();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        atlas = new TextureAtlas(map.getTexture(), map.getTileWidth(), map.getTileHeight());
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
        return 1;
    }

}
