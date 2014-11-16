package com.mojang.nbt;

import java.io.*;

import com.google.gson.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mostly Mojang AB
 */
public class NBTIntArrayTag extends NBTTag
{

    private int[] value;

    protected NBTIntArrayTag(String name)
    {
        this(name, null);
    }

    protected NBTIntArrayTag(String name, int[] data)
    {
        super(name);
        this.value = data;
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        dos.writeInt(value.length);
        for(int i = 0; i < value.length; i++ )
            dos.writeInt(value[i]);
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        value = new int[dis.readInt()];
        for(int i = 0; i < value.length; i++ )
            value[i] = dis.readInt();
    }

    @Override
    public String toString()
    {
        return "[" + value.length + " integers]";
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.INT_ARRAY;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTIntArrayTag(getName(), value);
    }

    public int[] getData()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTIntArrayTag o = (NBTIntArrayTag) obj;
            return ((value == null && o.value == null) || (value != null && value.equals(o.value)));
        }
        return false;
    }

    @Override
    public JsonElement toJson()
    {
        JsonArray array = new JsonArray();
        for(int i = 0; i < value.length; i++ )
            array.add(new JsonPrimitive(value[i]));
        return array;
    }

}
