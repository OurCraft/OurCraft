package org.craft.client.render.fonts;

public enum TextFormatting
{

    RESET('r'), ITALIC('i'), BOLD('b'), UNDERLINED('u'), COLOR('c', 8);

    public static char BEGINNING = '§';
    private char       s;
    private int        charsAfter;

    private TextFormatting(char s)
    {
        this(s, 0);
    }

    private TextFormatting(char s, int charsAfter)
    {
        this.s = s;
        this.charsAfter = charsAfter;
    }

    public int getCharsAfter()
    {
        return charsAfter;
    }

    public String toString()
    {
        return "" + s;
    }

    public static TextFormatting fromString(char id)
    {
        for(TextFormatting f : values())
        {
            if(f.s == id)
                return f;
        }
        return null;
    }

    public static String generateFromColor(int red, int green, int blue)
    {
        return generateFromColor(255, red, green, blue);
    }

    public static String generateFromColor(int alpha, int red, int green, int blue)
    {
        String alphaStr = Integer.toHexString(alpha);
        if(alphaStr.length() < 2)
            alphaStr = "0" + alphaStr;
        String redStr = Integer.toHexString(red);
        if(redStr.length() < 2)
            redStr = "0" + redStr;
        String greenStr = Integer.toHexString(green);
        if(greenStr.length() < 2)
            greenStr = "0" + greenStr;
        String blueStr = Integer.toHexString(blue);
        if(blueStr.length() < 2)
            blueStr = "0" + blueStr;
        return BEGINNING + COLOR.toString() + alphaStr + redStr + greenStr + blueStr;
    }
}
