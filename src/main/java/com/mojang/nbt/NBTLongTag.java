package com.mojang.nbt;

import java.io.*;

import com.google.gson.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mostly Mojang AB
 */
public class NBTLongTag extends NBTTag
{

    private long value;

    protected NBTLongTag(String name)
    {
        this(name, 0);
    }

    protected NBTLongTag(String name, long value)
    {
        super(name);
        this.value = value;
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        dos.writeLong(value);
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        value = dis.readLong();
    }

    @Override
    public String toString()
    {
        return "" + value;
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.LONG;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTLongTag(getName(), value);
    }

    public long getData()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTLongTag o = (NBTLongTag) obj;
            return o.value == value;
        }
        return false;
    }

    @Override
    public JsonElement toJson()
    {
        return new JsonPrimitive(value);
    }
}
