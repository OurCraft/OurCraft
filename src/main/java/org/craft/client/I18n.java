package org.craft.client;

import java.util.*;

import org.craft.resources.*;
import org.craft.utils.*;

public final class I18n
{

    private static HashMap<String, HashMap<String, String>> languages;
    private static String                                   current = "en_US";

    static
    {
        languages = new HashMap<String, HashMap<String, String>>();
    }

    public static void setCurrentLanguage(String id)
    {
        current = id;
    }

    public static void init(ResourceLoader loader) throws Exception
    {
        load(loader, "en_US");
        load(loader, "fr_FR");
    }

    private static void load(ResourceLoader loader, String id) throws Exception
    {
        AbstractResource res = loader.getResource(new ResourceLocation("ourcraft", "langs/" + id + ".lang"));
        String content = new String(res.getData(), "UTF-8");
        HashMap<String, String> lang = new HashMap<String, String>();
        StringBuffer buffer = new StringBuffer();
        for(String line : content.split("\n|\r"))
        {
            if(line.contains("="))
            {
                String key = null;
                for(int i = 0; i < line.length(); i++ )
                {
                    char c = line.charAt(i);
                    if(c == '=')
                    {
                        if(line.charAt(i - 1) != '\\' && key == null)
                        {
                            key = buffer.toString();
                            buffer.delete(0, buffer.length());
                        }
                    }
                    else
                        buffer.append(c);
                }
                lang.put(key, buffer.toString());
                buffer.delete(0, buffer.length());
                key = null;
            }
        }
        languages.put(id, lang);
    }

    public static String format(String message, Object... objects)
    {
        String translated = getCurrentLanguage().get(message);
        if(translated == null)
        {
            return message;
        }
        else
        {
            String result = translated;
            for(int i = 0; i < objects.length; i++ )
            {
                String indexInText = "%" + (i + 1) + "$";
                if(result.contains(indexInText))
                {
                    String newResult = "";
                    String before = result.substring(0, result.indexOf(indexInText));
                    String after = result.substring(result.indexOf(indexInText) + indexInText.length());
                    char type = after.charAt(0);
                    if(Character.toLowerCase(type) == 's') // Strings
                    {
                        newResult = objects[i].toString();
                    }
                    else if(Character.toLowerCase(type) == 'i') // Integers
                    {
                        newResult = ((Integer) objects[i]).toString();
                    }
                    else if(Character.toLowerCase(type) == 'f') // Floats
                    {
                        newResult = ((Float) objects[i]).toString();
                    }
                    else if(Character.toLowerCase(type) == 'l') // Longs
                    {
                        newResult = ((Long) objects[i]).toString();
                    }
                    else if(Character.toLowerCase(type) == 'd') // Doubles
                    {
                        newResult = ((Double) objects[i]).toString();
                    }
                    else if(Character.toLowerCase(type) == 'z') // Booleans
                    {
                        newResult = ((Boolean) objects[i]).toString();
                    }
                    after = after.substring(1);
                    result = before + newResult + after;
                }
                else
                {
                    Log.error("Could not find argument " + i + " in string: " + result);
                    break;
                }
            }
            return result;
        }
    }

    private static HashMap<String, String> getCurrentLanguage()
    {
        return languages.get(current);
    }
}
