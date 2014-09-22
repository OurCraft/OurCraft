package org.craft.client.render.fonts;

public enum TextFormatting
{

	RESET('r'), ITALIC('i'), BOLD('b'), UNDERLINED('u'), COLOR('c', 8);

	public static char BEGINNING = '§';
	private char	   s;
	private int		charsAfter;

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
			if(f.s == id) return f;
		}
		return null;
	}
}
