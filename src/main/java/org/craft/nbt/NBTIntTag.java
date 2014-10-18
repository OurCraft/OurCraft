package org.craft.nbt;

import java.io.*;

/**
 * Inspired by NBT classes given by Mojang AB here: https://mojang.com/2012/02/new-minecraft-map-format-anvil/
 * <br/>Following the specifications created by Markus 'notch' Personn: http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt
 * @author Mostly Mojang
 */
public class NBTIntTag extends NBTTag
{

    private int value;

    protected NBTIntTag(String name)
    {
        this(name, 0);
    }

    protected NBTIntTag(String name, int value)
    {
        super(name);
        this.value = value;
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        dos.writeInt(value);
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        value = dis.readInt();
    }

    @Override
    public String toString()
    {
        return "" + value;
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.INT;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTIntTag(getName(), value);
    }

    public int getData()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTIntTag o = (NBTIntTag) obj;
            return o.value == value;
        }
        return false;
    }
}
