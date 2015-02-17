package org.craft.client;

import java.util.*;

public class GameOption<T>
{

    private String                                      id;
    private T                                           value;
    private GameOptionType                              type;
    private T[]                                         possibleValues;
    private Runnable                                    callback;
    private static final HashMap<String, GameOption<?>> options = new HashMap<String, GameOption<?>>();

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

    public void setValue(T value)
    {
        this.value = value;
    }

    public T getValue()
    {
        return value;
    }

    public static GameOption<?> get(String id)
    {
        return options.get(id);
    }

    public void setPossibleValues(T[] possibleValues)
    {
        this.possibleValues = possibleValues;
    }

    public T[] getPossibleValues()
    {
        return possibleValues;
    }

    public void registerCallback(Runnable runnable)
    {
        this.callback = runnable;
    }

    public Runnable getCallback()
    {
        return callback;
    }

}
