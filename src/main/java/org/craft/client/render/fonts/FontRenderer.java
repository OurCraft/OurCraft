package org.craft.client.render.fonts;

import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;

public abstract class FontRenderer implements IDisposable
{

    private TextureAtlas atlas;
    private String       supportedChars;
    private OpenGLBuffer buffer;

    public FontRenderer(TextureAtlas atlas, String supportedChars)
    {
        this.atlas = atlas;
        this.supportedChars = supportedChars;
        this.buffer = new OpenGLBuffer();
    }

    public void drawShadowedString(String text, int color, int xo, int yo, RenderEngine renderEngine)
    {
        drawString(text, 0xFF000000, xo + 1, yo + 1, renderEngine);
        drawString(text, color, xo, yo, renderEngine);
    }

    public void drawString(String text, int color, int xo, int yo, RenderEngine renderEngine)
    {
        if(text.replace(" ", "").trim().isEmpty())
            return;
        int currentIndex = 0;
        float x = (float) xo;
        float y = (float) yo;

        boolean bold = false;
        boolean italic = false;
        boolean underlined = false;
        boolean obfuscated = false;

        int toSkip = 0;
        int currentColor = color;
        float r = (currentColor >> 16 & 0xFF) / 255f;
        float g = (currentColor >> 8 & 0xFF) / 255f;
        float b = (currentColor >> 0 & 0xFF) / 255f;
        Vector3 colorVec = Vector3.get(r, g, b);

        for(int i = 0; i < text.length(); i++ )
        {
            if(toSkip > 0)
            {
                toSkip-- ;
                continue;
            }
            char c = text.charAt(i);
            char next = '\0';
            if(i != text.length() - 1)
                next = text.charAt(i + 1);

            int lastIndex = currentIndex;
            if(c == TextFormatting.BEGINNING)
            {
                TextFormatting format = TextFormatting.fromString(next);
                if(format != null)
                {
                    String after = "";
                    for(int j = 0; j < format.getCharsAfter(); j++ )
                    {
                        after += text.charAt(j + i + 2);
                    }
                    toSkip = format.getCharsAfter() + 1;
                    if(format == TextFormatting.RESET)
                    {
                        obfuscated = false;
                        bold = false;
                        italic = false;
                        underlined = false;
                        currentColor = color;
                        r = (currentColor >> 16 & 0xFF) / 255f;
                        g = (currentColor >> 8 & 0xFF) / 255f;
                        b = (currentColor >> 0 & 0xFF) / 255f;
                        colorVec = Vector3.get(r, g, b);
                    }
                    else if(format == TextFormatting.OBFUSCATED)
                    {
                        obfuscated = !obfuscated;
                    }
                    else if(format == TextFormatting.COLOR)
                    {
                        currentColor = (int) Long.parseLong(after, 16);
                        r = (currentColor >> 16 & 0xFF) / 255f;
                        g = (currentColor >> 8 & 0xFF) / 255f;
                        b = (currentColor >> 0 & 0xFF) / 255f;
                        colorVec = Vector3.get(r, g, b);
                    }
                    else if(format == TextFormatting.ITALIC)
                    {
                        italic = true;
                    }
                    else if(format == TextFormatting.BOLD)
                    {
                        bold = true;
                    }
                    else if(format == TextFormatting.UNDERLINED)
                    {
                        underlined = true;
                    }
                    continue;
                }
            }

            if(underlined)
            {
                int index = getIndex('_');
                int xPos = index % atlas.getXNbr();
                int yPos = index / atlas.getXNbr();
                if(obfuscated)
                {
                    xPos = (int) (Math.random() * atlas.getXNbr());
                    yPos = (int) (Math.random() * atlas.getYNbr());
                }
                TextureRegion region = atlas.getTiles()[xPos][yPos];

                buffer.addVertex(new Vertex(Vector3.get(x - 2, y, 0), Vector2.get(region.getMinU(), region.getMaxV()), colorVec));
                buffer.addVertex(new Vertex(Vector3.get(x + 2 + getCharWidth(c), y, 0), Vector2.get(region.getMaxU(), region.getMaxV()), colorVec));
                buffer.addVertex(new Vertex(Vector3.get(x + 2 + getCharWidth(c), y + getCharHeight(c), 0), Vector2.get(region.getMaxU(), region.getMinV()), colorVec));
                buffer.addVertex(new Vertex(Vector3.get(x - 2, y + getCharHeight(c), 0), Vector2.get(region.getMinU(), region.getMinV()), colorVec));

                buffer.addIndex(currentIndex + 0);
                buffer.addIndex(currentIndex + 2);
                buffer.addIndex(currentIndex + 3);

                buffer.addIndex(currentIndex + 0);
                buffer.addIndex(currentIndex + 1);
                buffer.addIndex(currentIndex + 2);
                currentIndex += 4;

            }
            if(c == ' ')
            {
                x += getCharSpacing(c, next) + getCharWidth(c);
                continue;
            }
            int index = getIndex(c);
            if(index >= 0)
            {
                int xPos = index % atlas.getXNbr();
                int yPos = index / atlas.getXNbr();
                if(obfuscated)
                {
                    xPos = (int) (Math.random() * atlas.getXNbr());
                    yPos = (int) (Math.random() * atlas.getYNbr());
                }
                TextureRegion region = atlas.getTiles()[xPos][yPos];

                if(!italic)
                {
                    buffer.addVertex(new Vertex(Vector3.get(x, y, 0), Vector2.get(region.getMinU(), region.getMaxV()), colorVec));
                    buffer.addVertex(new Vertex(Vector3.get(x + getCharWidth(c), y, 0), Vector2.get(region.getMaxU(), region.getMaxV()), colorVec));
                    buffer.addVertex(new Vertex(Vector3.get(x + getCharWidth(c), y + getCharHeight(c), 0), Vector2.get(region.getMaxU(), region.getMinV()), colorVec));
                    buffer.addVertex(new Vertex(Vector3.get(x, y + getCharHeight(c), 0), Vector2.get(region.getMinU(), region.getMinV()), colorVec));
                }
                else
                {
                    float italicFactor = -2.5f;
                    buffer.addVertex(new Vertex(Vector3.get(x - italicFactor, y, 0), Vector2.get(region.getMinU(), region.getMaxV()), colorVec));
                    buffer.addVertex(new Vertex(Vector3.get(x + getCharWidth(c) - italicFactor, y, 0), Vector2.get(region.getMaxU(), region.getMaxV()), colorVec));
                    buffer.addVertex(new Vertex(Vector3.get(x + getCharWidth(c), y + getCharHeight(c), 0), Vector2.get(region.getMaxU(), region.getMinV()), colorVec));
                    buffer.addVertex(new Vertex(Vector3.get(x + italicFactor, y + getCharHeight(c), 0), Vector2.get(region.getMinU(), region.getMinV()), colorVec));
                }

                buffer.addIndex(currentIndex + 0);
                buffer.addIndex(currentIndex + 2);
                buffer.addIndex(currentIndex + 3);

                buffer.addIndex(currentIndex + 0);
                buffer.addIndex(currentIndex + 1);
                buffer.addIndex(currentIndex + 2);

                currentIndex += 4;

                x += getCharWidth(c) + getCharSpacing(c, next);
                x = (float) Math.floor(x);
            }
        }
        buffer.upload();
        renderEngine.renderBuffer(buffer, atlas.getTexture());
        colorVec.dispose();
        buffer.clearAndDisposeVertices();
    }

    private int getIndex(char c)
    {
        if(supportedChars == null)
        {
            return c;
        }
        else
            return supportedChars.indexOf(c);
    }

    public double getCharSpacing(char c, char next)
    {
        return 0;
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    public String getSupportedChars()
    {
        return supportedChars;
    }

    public abstract float getCharWidth(char c);

    public abstract float getCharHeight(char c);

    public float getTextLength(String text)
    {
        float l = 0;
        int toSkip = 0;
        for(int i = 0; i < text.length(); i++ )
        {
            if(toSkip > 0)
            {
                toSkip-- ;
                continue;
            }
            char c = text.charAt(i);
            char next = '\0';
            if(text.length() - 1 != i)
                next = text.charAt(i + 1);
            if(c == TextFormatting.BEGINNING)
            {
                TextFormatting format = TextFormatting.fromString(next);
                if(format != null)
                {
                    toSkip = format.getCharsAfter() + 1;
                    continue;
                }
            }
            l += getCharWidth(c) + getCharSpacing(c, next);
        }
        return l;
    }

    public void dispose()
    {
        buffer.dispose();
    }
}
