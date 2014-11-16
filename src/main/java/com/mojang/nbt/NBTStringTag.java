package com.mojang.nbt;

import java.io.*;

import com.google.gson.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mostly Mojang AB
 */
public class NBTStringTag extends NBTTag
{

    private String value;

    protected NBTStringTag(String name)
    {
        super(name);
    }

    protected NBTStringTag(String name, String value)
    {
        super(name);
        this.value = value;
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        dos.writeUTF(value);
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        value = dis.readUTF();
    }

    @Override
    public String toString()
    {
        return value;
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.STRING;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTStringTag(getName(), value);
    }

    public String getData()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTStringTag o = (NBTStringTag) obj;
            return value.equals(o.value);
        }
        return false;
    }

    @Override
    public JsonElement toJson()
    {
        return new JsonPrimitive(value);
    }
}
