package org.craft.nbt;

import java.io.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mostly Mojang AB
 */
public class NBTByteTag extends NBTTag
{

    private byte value;

    protected NBTByteTag(String name)
    {
        this(name, (byte) 0);
    }

    protected NBTByteTag(String name, byte value)
    {
        super(name);
        this.value = value;
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        dos.writeByte(value);
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        value = dis.readByte();
    }

    @Override
    public String toString()
    {
        return "" + value;
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.BYTE;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTByteTag(getName(), value);
    }

    public byte getData()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTByteTag o = (NBTByteTag) obj;
            return o.value == value;
        }
        return false;
    }

}
