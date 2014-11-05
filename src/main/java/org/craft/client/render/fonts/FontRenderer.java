package org.craft.client.render.fonts;

import java.util.*;

import org.craft.client.render.*;
import org.craft.maths.*;
import org.craft.utils.*;

public abstract class FontRenderer implements IDisposable
{

    private final static class TextInfos
    {
        public String text;
        public int    color;
        public int    posX;
        public int    posY;
        public float  scale;

        public TextInfos()
        {

        }

        public int hashCode()
        {
            final int BASE = 17;
            final int MULTIPLIER = 31;

            int result = BASE;
            result = MULTIPLIER * result + posX;
            result = MULTIPLIER * result + posY;
            result = MULTIPLIER * result + color;
            result = MULTIPLIER * result + Float.floatToIntBits(scale);
            result = MULTIPLIER * result + text.hashCode();

            return result;
        }

        public boolean equals(Object o)
        {
            if(o instanceof TextInfos)
            {
                TextInfos infos = (TextInfos) o;
                return infos.text.equals(text) && infos.color == color && infos.posX == posX && infos.posY == posY && infos.scale == scale;
            }
            return false;
        }

    }

    protected TextureAtlas                   atlas;
    protected String                         supportedChars;
    protected OpenGLBuffer                   buffer;
    private HashMap<TextInfos, OpenGLBuffer> cache;
    private TextInfos                        textInfos;
    private float                            scale;

    /**
     * Creates font renderer for given supportedChars and given texture atlas
     */
    public FontRenderer(TextureAtlas atlas, String supportedChars)
    {
        this.scale = 1f;
        textInfos = new TextInfos();
        cache = new HashMap<TextInfos, OpenGLBuffer>();
        this.atlas = atlas;
        this.supportedChars = supportedChars;
        this.buffer = new OpenGLBuffer();
    }

    /**
     * Draws a string with a shadow behind it
     */
    public void drawShadowedString(String text, int color, int xo, int yo, RenderEngine renderEngine)
    {
        drawString(text, 0xFF000000, (int) (xo + scale), (int) (yo + scale), renderEngine);
        drawString(text, color, xo, yo, renderEngine);
    }

    /**
     * Draws a string at given coordinates and given color
     */
    public void drawString(String text, int color, int xo, int yo, RenderEngine renderEngine)
    {
        if(text.replace(" ", "").trim().isEmpty())
            return;
        String colorAsHex = Integer.toHexString(color);
        for(int i = 8; i > colorAsHex.length(); i-- )
            colorAsHex = '0' + colorAsHex;
        text = TextFormatting.COLOR.toString() + colorAsHex + text;
        textInfos.text = text;
        textInfos.color = color;
        textInfos.posX = xo;
        textInfos.posY = yo;
        textInfos.scale = scale;
        if(!text.contains(TextFormatting.OBFUSCATED.toString()))
        {
            if(cache.containsKey(textInfos))
            {
                renderEngine.renderBuffer(cache.get(textInfos), atlas.getTexture());
                return;
            }
        }
        buffer.clearAndDisposeVertices();

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

            if(c == TextFormatting.BEGINNING)
            {
                TextFormatting format = TextFormatting.fromChar(next);
                if(format != null)
                {
                    String after = "";
                    for(int j = 0; j < format.charsAfter(); j++ )
                    {
                        after += text.charAt(j + i + 2);
                    }
                    toSkip = format.charsAfter() + 1;
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

                buffer.addVertex(Vertex.get(Vector3.get(x - 2 * scale, y, 0), Vector2.get(region.getMinU(), region.getMaxV()), colorVec));
                buffer.addVertex(Vertex.get(Vector3.get(x + (2 + getCharWidth(c)) * scale, y, 0), Vector2.get(region.getMaxU(), region.getMaxV()), colorVec));
                buffer.addVertex(Vertex.get(Vector3.get(x + (2 + getCharWidth(c)) * scale, y + getCharHeight(c) * scale, 0), Vector2.get(region.getMaxU(), region.getMinV()), colorVec));
                buffer.addVertex(Vertex.get(Vector3.get(x - 2 * scale, y + (getCharHeight(c)) * scale, 0), Vector2.get(region.getMinU(), region.getMinV()), colorVec));

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
                x += getCharSpacing(c, next) * scale + getCharWidth(c) * scale;
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
                    buffer.addVertex(Vertex.get(Vector3.get(x, y, 0), Vector2.get(region.getMinU(), region.getMaxV()), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + getCharWidth(c) * scale, y, 0), Vector2.get(region.getMaxU(), region.getMaxV()), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + getCharWidth(c) * scale, y + getCharHeight(c) * scale, 0), Vector2.get(region.getMaxU(), region.getMinV()), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x, y + getCharHeight(c) * scale, 0), Vector2.get(region.getMinU(), region.getMinV()), colorVec));
                }
                else
                {
                    float italicFactor = -2.5f;
                    buffer.addVertex(Vertex.get(Vector3.get(x - italicFactor * scale, y, 0), Vector2.get(region.getMinU(), region.getMaxV()), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + (getCharWidth(c) - italicFactor) * scale, y, 0), Vector2.get(region.getMaxU(), region.getMaxV()), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + getCharWidth(c) * scale, y + getCharHeight(c) * scale, 0), Vector2.get(region.getMaxU(), region.getMinV()), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + italicFactor * scale, y + getCharHeight(c) * scale, 0), Vector2.get(region.getMinU(), region.getMinV()), colorVec));
                }

                buffer.addIndex(currentIndex + 0);
                buffer.addIndex(currentIndex + 2);
                buffer.addIndex(currentIndex + 3);

                buffer.addIndex(currentIndex + 0);
                buffer.addIndex(currentIndex + 1);
                buffer.addIndex(currentIndex + 2);

                currentIndex += 4;

                x += Math.floor(getCharWidth(c) * scale + getCharSpacing(c, next) * scale);
            }
        }
        buffer.upload();
        renderEngine.renderBuffer(buffer, atlas.getTexture());
        colorVec.dispose();
        if(!text.contains(TextFormatting.OBFUSCATED.toString()))
        {
            TextInfos textInfos1 = new TextInfos();
            textInfos1.text = text;
            textInfos1.color = color;
            textInfos1.posX = xo;
            textInfos1.posY = yo;
            textInfos.scale = scale;
            cache.put(textInfos1, buffer);
            buffer = new OpenGLBuffer();
        }
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public float getScale()
    {
        return scale;
    }

    protected int getIndex(char c)
    {
        if(supportedChars == null)
        {
            return c;
        }
        else
            return supportedChars.indexOf(c);
    }

    /**
     * Gets spacing between given chars
     */
    public double getCharSpacing(char c, char next)
    {
        return 0;
    }

    /**
     * Returns texture atlas used to render text
     */
    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    /**
     * Gets all supported chars by this font <b>or null if all are supported.</b> 
     */
    public String getSupportedChars()
    {
        return supportedChars;
    }

    /**
     * Gets the width of given char
     */
    public abstract float getCharWidth(char c);

    /**
     * Gets the height of given char
     */
    public abstract float getCharHeight(char c);

    /**
     * Gets the width of given text
     */
    public float getTextWidth(String text)
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
                TextFormatting format = TextFormatting.fromChar(next);
                if(format != null)
                {
                    toSkip = format.charsAfter() + 1;
                    continue;
                }
            }
            l += getCharWidth(c) * scale + getCharSpacing(c, next) * scale;
        }
        return l;
    }

    public void disposeCache()
    {
        for(OpenGLBuffer cachedBuffer : cache.values())
            cachedBuffer.dispose();
        cache.clear();
    }

    public void dispose()
    {
        disposeCache();
        buffer.dispose();
    }
}
