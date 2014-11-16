package com.mojang.nbt;

import java.io.*;

import com.google.gson.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mostly Mojang AB
 */
public class NBTShortTag extends NBTTag
{

    private short value;

    protected NBTShortTag(String name)
    {
        this(name, (short) 0);
    }

    protected NBTShortTag(String name, short value)
    {
        super(name);
        this.value = value;
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        dos.writeShort(value);
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        value = dis.readShort();
    }

    @Override
    public String toString()
    {
        return "" + value;
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.SHORT;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTShortTag(getName(), value);
    }

    public short getData()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTShortTag o = (NBTShortTag) obj;
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
