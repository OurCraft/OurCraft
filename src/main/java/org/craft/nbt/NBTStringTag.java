package org.craft.nbt;

import java.io.*;

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
}
