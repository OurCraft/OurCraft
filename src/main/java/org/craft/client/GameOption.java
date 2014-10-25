package org.craft.client;

import java.util.*;

public class GameOption
{

    private String                                   id;
    private String                                   value;
    private static final HashMap<String, GameOption> options = new HashMap<String, GameOption>();

    public GameOption(String id)
    {
        this.id = id;
        options.put(id, this);
    }

    public String getID()
    {
        return id;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static GameOption get(String id)
    {
        return options.get(id);
    }

    public int getValueAsInt()
    {
        return Integer.parseInt(value);
    }
}
