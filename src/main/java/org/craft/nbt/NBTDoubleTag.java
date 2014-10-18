package org.craft.nbt;

import java.io.*;

/**
 * Inspired by NBT classes given by Mojang AB here: https://mojang.com/2012/02/new-minecraft-map-format-anvil/
 * <br/>Following the specifications created by Markus 'notch' Personn: http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt
 * @author Mostly Mojang
 */
public class NBTDoubleTag extends NBTTag
{

    private double value;

    protected NBTDoubleTag(String name)
    {
        this(name, 0);
    }

    protected NBTDoubleTag(String name, double value)
    {
        super(name);
        this.value = value;
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        dos.writeDouble(value);
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        value = dis.readDouble();
    }

    @Override
    public String toString()
    {
        return "" + value;
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.DOUBLE;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTDoubleTag(getName(), value);
    }

    public double getData()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTDoubleTag o = (NBTDoubleTag) obj;
            return o.value == value;
        }
        return false;
    }

}
