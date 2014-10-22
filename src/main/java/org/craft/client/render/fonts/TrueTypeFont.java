package org.craft.client.render.fonts;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

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

    /** Font's width */
    private int                            fontWidth;

    /** Font's height */
    private int                            fontHeight          = 0;

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
    public BufferedImage getFontImage(char ch)
    {
        // Create a temporary image to extract the character's size
        BufferedImage tempfontImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
        if(antiAlias)
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
        if(antiAlias)
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
        int customCharsLength = (customCharsArray != null) ? customCharsArray.length : 0;

        for(int i = 0; i < 256 + customCharsLength; i++ )
        {
            // get 0-255 characters and then custom characters
            char ch = (i < 256) ? (char) i : customCharsArray[i - 256];

            BufferedImage fontImage = getFontImage(ch);

            CharProperties charProps = new CharProperties();

            charProps.width = fontImage.getWidth();
            charProps.height = fontImage.getHeight();

            if(charProps.height > fontHeight)
            {
                fontHeight = charProps.height;
            }

            if(charProps.width > fontWidth)
            {
                fontWidth = charProps.width;
            }
            if(i < 256)
                charPropertiesArray[i] = charProps;
            else
                customChars.put(new Character(ch), charProps);

            //Force clean tmpImg
            fontImage.flush();
        }
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
        return fontWidth;
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

    /**
     * Returns name of font
     */
    public String getName()
    {
        return font.getFontName();
    }

    private static class CharProperties
    {
        public int width, height;
    }
}
