package org.craft.utils;

public class StringUtils
{

	public static String removeSpacesAtStart(String line)
	{
		while(!line.isEmpty() && line.startsWith(" "))
			line = line.substring(1);
		return line;
	}

	public static String createCorrectedFileName(String name)
	{
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < name.length(); i++ )
		{
			char c = name.charAt(i);
			if(c == ':' || c == '?' || c == '/' || c == '\\') c = '_';
			buffer.append(c);
		}

		return buffer.toString();
	}
}
