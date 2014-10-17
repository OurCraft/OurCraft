package org.craft.client.render.fonts;

import java.awt.Font;

import org.craft.client.render.RenderEngine;
import org.craft.client.render.Texture;
import org.craft.client.render.TextureRegion;
import org.craft.client.render.Vertex;
import org.craft.maths.Vector2;
import org.craft.maths.Vector3;

public class TrueTypeFontRenderer extends FontRenderer
{

    private TrueTypeFont font;

    public TrueTypeFontRenderer(String fontName)
    {
        super(null, null);
        Font awtFont = new Font("Times New Roman", Font.TRUETYPE_FONT, 16);
        font = new TrueTypeFont(awtFont, false);
        // TODO Auto-generated constructor stub
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

    @Override
    public void drawString(String text, int color, int xo, int yo, RenderEngine renderEngine)
    {
        if(text.replace(" ", "").trim().isEmpty())
            return;
        int currentIndex = 0;
        float x = (float) xo;
        float y = (float) yo;

        boolean italic = false;
        boolean underlined = false;
        boolean obfuscated = false;

        int toSkip = 0;
        int currentColor = color;
        float r = (currentColor >> 16 & 0xFF) / 255f;
        float g = (currentColor >> 8 & 0xFF) / 255f;
        float b = (currentColor >> 0 & 0xFF) / 255f;
        Vector3 colorVec = Vector3.get(r, g, b);

        Texture texture = null;
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
                float w = getCharWidth(c);
                float h = getCharHeight(c);
                float minU = (x * (w)) / (float) texture.getWidth();
                float minV = (y * (h) + (h)) / (float) texture.getHeight();

                float maxU = (x * (w) + (w)) / (float) texture.getWidth();
                float maxV = (y * (h)) / (float) texture.getHeight();
                buffer.addVertex(Vertex.get(Vector3.get(x - 2, y, 0), Vector2.get(minU, maxV), colorVec));
                buffer.addVertex(Vertex.get(Vector3.get(x + 2 + w, y, 0), Vector2.get(maxU, maxV), colorVec));
                buffer.addVertex(Vertex.get(Vector3.get(x + 2 + w, y + h, 0), Vector2.get(maxU, minV), colorVec));
                buffer.addVertex(Vertex.get(Vector3.get(x - 2, y + h, 0), Vector2.get(minU, minV), colorVec));

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
                texture = font.getCharTexture(c);
                float w = texture.getWidth();
                float h = texture.getHeight();
                int xPos = index % 16;
                int yPos = index / 16;
                float minU = (xPos * (w)) / (float) texture.getWidth();
                float minV = (yPos * (h) + (h)) / (float) texture.getHeight();

                float maxU = (xPos * (w) + (w)) / (float) texture.getWidth();
                float maxV = (yPos * (h)) / (float) texture.getHeight();

                System.out.println("Char : " + c + " w=" + w + " h=" + h);

                if(!italic)
                {
                    buffer.addVertex(Vertex.get(Vector3.get(x, y, 0), Vector2.get(minU, maxV), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + w, y, 0), Vector2.get(maxU, maxV), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + w, y + h, 0), Vector2.get(maxU, minV), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x, y + h, 0), Vector2.get(minU, minV), colorVec));
                }
                else
                {
                    float italicFactor = -2.5f;
                    buffer.addVertex(Vertex.get(Vector3.get(x - italicFactor, y, 0), Vector2.get(minU, maxV), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + w - italicFactor, y, 0), Vector2.get(maxU, maxV), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + w, y + h, 0), Vector2.get(maxU, minV), colorVec));
                    buffer.addVertex(Vertex.get(Vector3.get(x + italicFactor, y + h, 0), Vector2.get(minU, minV), colorVec));
                }

                buffer.addIndex(currentIndex + 0);
                buffer.addIndex(currentIndex + 2);
                buffer.addIndex(currentIndex + 3);

                buffer.addIndex(currentIndex + 0);
                buffer.addIndex(currentIndex + 1);
                buffer.addIndex(currentIndex + 2);

                currentIndex += 4;

                x += w + getCharSpacing(c, next);
                x = (float) Math.floor(x);
            }
            buffer.upload();
            renderEngine.renderBuffer(buffer, texture);
            colorVec.dispose();
            buffer.clearAndDisposeVertices();
        }

    }

    public double getCharSpacing(char c, char next)
    {
        return 2;
    }

}
