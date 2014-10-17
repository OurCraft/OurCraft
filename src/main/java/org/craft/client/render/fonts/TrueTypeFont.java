package org.craft.client.render.fonts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.craft.client.OpenGLHelper;
import org.craft.client.render.Texture;

import sun.nio.ch.IOUtil;

public class TrueTypeFont
{
    /** Array that holds necessary information about the font characters */
    private CharProperties[]               charPropertiesArray = new CharProperties[256];

    /** Map of user defined font characters (Character <-> IntObject) */
    private Map<Character, CharProperties> customChars         = new HashMap<Character, CharProperties>();
    /** Boolean flag on whether AntiAliasing is enabled or not */
    private boolean                        antiAlias;

    /** Font's size */
    private int                            fontSize            = 0;

    /** Font's height */
    private int                            fontHeight          = 0;

    /** Texture used to cache the font 0-255 characters */
    private Texture                        fontTexture;

    /** Default font texture width */
    private int                            textureWidth        = 512;

    /** Default font texture height */
    private int                            textureHeight       = 512;

    /** A reference to Java's AWT Font that we create our font texture from */
    private Font                           font;

    /** The font metrics for our Java AWT font */
    private FontMetrics                    fontMetrics;

    public TrueTypeFont(Font font, boolean antiAlias)
    {
        this(font, antiAlias, null);
    }

    public TrueTypeFont(Font font, boolean antiAlias, char[] charArray)
    {
        this.font = font;
        this.fontSize = font.getSize();
        this.antiAlias = antiAlias;
        this.createSet(charArray);
    }

    /**
     * Create a standard Java2D BufferedImage of the given character
     * 
     * @param ch
     *            The character to create a BufferedImage for
     * 
     * @return A BufferedImage containing the character
     */
    private BufferedImage getFontImage(char ch)
    {
        // Create a temporary image to extract the character's size
        BufferedImage tempfontImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
        if(antiAlias == true)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        fontMetrics = g.getFontMetrics();
        int charwidth = fontMetrics.charWidth(ch);

        if(charwidth <= 0)
        {
            charwidth = 1;
        }
        int charheight = fontMetrics.getHeight();
        if(charheight <= 0)
        {
            charheight = fontSize;
        }

        // Create another image holding the character we are creating
        BufferedImage fontImage;
        fontImage = new BufferedImage(charwidth, charheight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D gt = (Graphics2D) fontImage.getGraphics();
        if(antiAlias == true)
        {
            gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        gt.setFont(font);

        gt.setColor(Color.WHITE);
        int charx = 0;
        int chary = 0;
        gt.drawString(String.valueOf(ch), (charx), (chary)
                + fontMetrics.getAscent());

        return fontImage;

    }

    /**
     * Create and store the font
     * 
     * @param customCharsArray Characters that should be also added to the cache.
     */
    private void createSet(char[] customCharsArray)
    {
        if(customCharsArray != null && customCharsArray.length > 0)
        {
            textureWidth *= 2;
        }

        BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) imgTemp.getGraphics();

        g.setColor(new Color(255, 255, 255, 1));
        g.fillRect(0, 0, textureWidth, textureHeight);

        int rowHeight = 0;
        int positionX = 0;
        int positionY = 0;

        int customCharsLength = (customCharsArray != null) ? customCharsArray.length : 0;

        for(int i = 0; i < 256 + customCharsLength; i++ )
        {

            // get 0-255 characters and then custom characters
            char ch = (i < 256) ? (char) i : customCharsArray[i - 256];

            BufferedImage fontImage = getFontImage(ch);

            CharProperties newIntObject = new CharProperties();

            newIntObject.width = fontImage.getWidth();
            newIntObject.height = fontImage.getHeight();

            if(positionX + newIntObject.width >= textureWidth)
            {
                positionX = 0;
                positionY += rowHeight;
                rowHeight = 0;
            }

            newIntObject.x = positionX;
            newIntObject.y = positionY;

            if(newIntObject.height > fontHeight)
            {
                fontHeight = newIntObject.height;
            }

            if(newIntObject.height > rowHeight)
            {
                rowHeight = newIntObject.height;
            }

            // Draw it to buffer!
            g.drawImage(fontImage, positionX, positionY, null);

            positionX += newIntObject.width;

            if(i < 256)
                charPropertiesArray[i] = newIntObject;
            else
                customChars.put(new Character(ch), newIntObject);

            //Force clean tmpImg
            fontImage = null;
        }
        fontTexture = OpenGLHelper.loadTexture(imgTemp);
    }

    public Texture getTexture()
    {
        return fontTexture;
    }

    public float getCharWidth(char c)
    {
        CharProperties charProperty = null;
        if(c < 256)
            charProperty = charPropertiesArray[c];
        else
            charProperty = customChars.get(c);
        if(charProperty != null)
            return charProperty.width;
        return fontHeight;
    }

    public float getCharHeight(char c)
    {
        CharProperties charProperty = null;
        if(c < 256)
            charProperty = charPropertiesArray[c];
        else
            charProperty = customChars.get(c);

        if(charProperty != null)
            return charProperty.height;
        return fontHeight;
    }

    public Texture getCharTexture(char c)
    {
        Texture result = null;
        CharProperties charProperty = null;
        if(c < 256)
            charProperty = charPropertiesArray[c];
        else
            charProperty = customChars.get(c);

        result = charProperty.texture;
        if(result == null)
        {
            charProperty.texture = result;
            result = OpenGLHelper.loadTexture(getFontImage(c));
        }

        return result;
    }

}

class CharProperties
{
    public int     width, height, x, y;
    public Texture texture;
}
