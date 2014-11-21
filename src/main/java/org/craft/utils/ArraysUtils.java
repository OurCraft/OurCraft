package org.craft.utils;

import java.util.ArrayList;

public final class ArraysUtils
{

    public static String[] trim(String[] split)
    {
        ArrayList<String> strings = new ArrayList<String>();
        for(String s : split)
            if(s != null && !s.trim().equals(""))
                strings.add(s);
        return strings.toArray(new String[0]);
    }

    public static String sum(String[] tokens, int start, int length, String separator)
    {
        StringBuffer buffer = new StringBuffer();
        for(int i = start; i < length + start; i++ )
        {
            buffer.append(tokens[i]);
            if(i != length + start - 1)
            {
                buffer.append(separator);
            }
        }
        return buffer.toString();
    }

}
