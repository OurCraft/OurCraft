package org.craft.nbt;

import java.io.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mostly Mojang AB
 */
public class NBTFloatTag extends NBTTag
{

    private float value;

    protected NBTFloatTag(String name)
    {
        this(name, 0);
    }

    protected NBTFloatTag(String name, float value)
    {
        super(name);
        this.value = value;
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        dos.writeFloat(value);
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        value = dis.readFloat();
    }

    @Override
    public String toString()
    {
        return "" + value;
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.FLOAT;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTFloatTag(getName(), value);
    }

    public float getData()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTFloatTag o = (NBTFloatTag) obj;
            return o.value == value;
        }
        return false;
    }

}
