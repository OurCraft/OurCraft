package org.craft.client.render.fonts;

import java.util.*;

import org.craft.client.render.*;
import org.craft.maths.*;

public abstract class FontRenderer
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

    /**
     * Draws a given string at given coords with given color
     */
    public void drawString(String text, int color, int xo, int yo, RenderEngine renderEngine)
    {
        drawString(text, color, xo, yo, false, renderEngine);
    }

    public void drawShadowedString(String text, int color, int xo, int yo, RenderEngine renderEngine)
    {
        drawString(text, color, xo, yo, true, renderEngine);
    }

    public void drawString(String text, int color, int xo, int yo, boolean shadowed, RenderEngine renderEngine)
    {
        buffer.clearAndDisposeVertices();
        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
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

                if(shadowed)
                {
                    vertices.add(new Vertex(Vector3.get(x - 2 + 1, y + 1, 0), Vector2.get(region.getMinU(), region.getMaxV()), Vector3.get(0, 0, 0)));
                    vertices.add(new Vertex(Vector3.get(x + 2 + 1 + getCharWidth(c), y + 1, 0), Vector2.get(region.getMaxU(), region.getMaxV()), Vector3.get(0, 0, 0)));
                    vertices.add(new Vertex(Vector3.get(x + 2 + 1 + getCharWidth(c), y + 1 + getCharHeight(c), 0), Vector2.get(region.getMaxU(), region.getMinV()), Vector3.get(0, 0, 0)));
                    vertices.add(new Vertex(Vector3.get(x - 2 + 1, y + 1 + getCharHeight(c), 0), Vector2.get(region.getMinU(), region.getMinV()), Vector3.get(0, 0, 0)));

                    indices.add(currentIndex + 0);
                    indices.add(currentIndex + 2);
                    indices.add(currentIndex + 3);

                    indices.add(currentIndex + 0);
                    indices.add(currentIndex + 1);
                    indices.add(currentIndex + 2);
                    currentIndex += 4;
                }

                vertices.add(new Vertex(Vector3.get(x - 2, y, 0), Vector2.get(region.getMinU(), region.getMaxV()), colorVec));
                vertices.add(new Vertex(Vector3.get(x + 2 + getCharWidth(c), y, 0), Vector2.get(region.getMaxU(), region.getMaxV()), colorVec));
                vertices.add(new Vertex(Vector3.get(x + 2 + getCharWidth(c), y + getCharHeight(c), 0), Vector2.get(region.getMaxU(), region.getMinV()), colorVec));
                vertices.add(new Vertex(Vector3.get(x - 2, y + getCharHeight(c), 0), Vector2.get(region.getMinU(), region.getMinV()), colorVec));

                indices.add(currentIndex + 0);
                indices.add(currentIndex + 2);
                indices.add(currentIndex + 3);

                indices.add(currentIndex + 0);
                indices.add(currentIndex + 1);
                indices.add(currentIndex + 2);
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

                if(shadowed)
                {
                    if(!italic)
                    {
                        vertices.add(new Vertex(Vector3.get(x + 1, y + 1, 0), Vector2.get(region.getMinU(), region.getMaxV()), Vector3.get(0, 0, 0)));
                        vertices.add(new Vertex(Vector3.get(x + 1 + getCharWidth(c), y + 1, 0), Vector2.get(region.getMaxU(), region.getMaxV()), Vector3.get(0, 0, 0)));
                        vertices.add(new Vertex(Vector3.get(x + 1 + getCharWidth(c), y + 1 + getCharHeight(c), 0), Vector2.get(region.getMaxU(), region.getMinV()), Vector3.get(0, 0, 0)));
                        vertices.add(new Vertex(Vector3.get(x + 1, y + 1 + getCharHeight(c), 0), Vector2.get(region.getMinU(), region.getMinV()), Vector3.get(0, 0, 0)));
                    }
                    else
                    {
                        float italicFactor = -2.5f;
                        vertices.add(new Vertex(Vector3.get(x + 1 - italicFactor, y + 1, 0), Vector2.get(region.getMinU(), region.getMaxV()), Vector3.get(0, 0, 0)));
                        vertices.add(new Vertex(Vector3.get(x + 1 + getCharWidth(c) - italicFactor, y + 1, 0), Vector2.get(region.getMaxU(), region.getMaxV()), Vector3.get(0, 0, 0)));
                        vertices.add(new Vertex(Vector3.get(x + 1 + getCharWidth(c), y + 1 + getCharHeight(c), 0), Vector2.get(region.getMaxU(), region.getMinV()), Vector3.get(0, 0, 0)));
                        vertices.add(new Vertex(Vector3.get(x + 1 + italicFactor, y + 1 + getCharHeight(c), 0), Vector2.get(region.getMinU(), region.getMinV()), Vector3.get(0, 0, 0)));
                    }

                    indices.add(currentIndex + 0);
                    indices.add(currentIndex + 2);
                    indices.add(currentIndex + 3);

                    indices.add(currentIndex + 0);
                    indices.add(currentIndex + 1);
                    indices.add(currentIndex + 2);

                    currentIndex += 4;
                }

                if(!italic)
                {
                    vertices.add(new Vertex(Vector3.get(x, y, 0), Vector2.get(region.getMinU(), region.getMaxV()), colorVec));
                    vertices.add(new Vertex(Vector3.get(x + getCharWidth(c), y, 0), Vector2.get(region.getMaxU(), region.getMaxV()), colorVec));
                    vertices.add(new Vertex(Vector3.get(x + getCharWidth(c), y + getCharHeight(c), 0), Vector2.get(region.getMaxU(), region.getMinV()), colorVec));
                    vertices.add(new Vertex(Vector3.get(x, y + getCharHeight(c), 0), Vector2.get(region.getMinU(), region.getMinV()), colorVec));
                }
                else
                {
                    float italicFactor = -2.5f;
                    vertices.add(new Vertex(Vector3.get(x - italicFactor, y, 0), Vector2.get(region.getMinU(), region.getMaxV()), colorVec));
                    vertices.add(new Vertex(Vector3.get(x + getCharWidth(c) - italicFactor, y, 0), Vector2.get(region.getMaxU(), region.getMaxV()), colorVec));
                    vertices.add(new Vertex(Vector3.get(x + getCharWidth(c), y + getCharHeight(c), 0), Vector2.get(region.getMaxU(), region.getMinV()), colorVec));
                    vertices.add(new Vertex(Vector3.get(x + italicFactor, y + getCharHeight(c), 0), Vector2.get(region.getMinU(), region.getMinV()), colorVec));
                }

                indices.add(currentIndex + 0);
                indices.add(currentIndex + 2);
                indices.add(currentIndex + 3);

                indices.add(currentIndex + 0);
                indices.add(currentIndex + 1);
                indices.add(currentIndex + 2);

                currentIndex += 4;

                x += getCharWidth(c) + getCharSpacing(c, next);
                x = (float) Math.floor(x);
            }
        }
        buffer.setVertices(vertices);
        buffer.setIndices(indices);
        buffer.upload();
        renderEngine.renderBuffer(buffer, atlas.getTexture());
        vertices.clear();
        indices.clear();
        colorVec.dispose();
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
        for(int i = 0; i < text.length(); i++ )
        {
            char c = text.charAt(i);
            char next = '\0';
            if(text.length() - 1 != i)
                next = text.charAt(i + 1);
            l += getCharWidth(c) + getCharSpacing(c, next);
        }
        return l;
    }
}
