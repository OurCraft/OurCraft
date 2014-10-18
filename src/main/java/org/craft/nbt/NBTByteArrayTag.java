package org.craft.nbt;

import java.io.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mostly Mojang AB
 */
public class NBTByteArrayTag extends NBTTag
{

    private byte[] data;

    protected NBTByteArrayTag(String name)
    {
        this(name, null);
    }

    protected NBTByteArrayTag(String name, byte[] data)
    {
        super(name);
        this.data = data;
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        dos.writeInt(data.length);
        dos.write(data);
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        data = new byte[dis.readInt()];
        dis.readFully(data);
    }

    @Override
    public String toString()
    {
        return "[" + data.length + " bytes]";
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.BYTE_ARRAY;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTByteArrayTag(getName(), data);
    }

    public byte[] getData()
    {
        return data;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTByteArrayTag o = (NBTByteArrayTag) obj;
            return ((data == null && o.data == null) || (data != null && data.equals(o.data)));
        }
        return false;
    }

}
