package org.craft.client;

import java.util.*;

public class GameOption
{

    private String                                   id;
    private String                                   value;
    private GameOptionType                               type;
    private static final HashMap<String, GameOption> options = new HashMap<String, GameOption>();

    public GameOption(String id, GameOptionType type)
    {
        this.id = id;
        this.type = type;
        options.put(id, this);
    }

    public GameOptionType getType()
    {
        return type;
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

    public float getValueAsFloat()
    {
        return Float.parseFloat(value);
    }
}
