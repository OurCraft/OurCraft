package org.craft.nbt;

import java.io.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mostly Mojang AB
 */
public abstract class NBTTag implements Cloneable
{

    private String name;

    protected NBTTag(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    abstract void write(DataOutput dos) throws IOException;

    abstract void read(DataInput dis) throws IOException;

    public abstract String toString();

    public abstract NBTTypes getID();

    public boolean equals(Object o)
    {
        if(o instanceof NBTTag)
        {
            NBTTag tag = (NBTTag) o;
            return tag.getName().equals(name) && tag.getID() == getID();
        }
        return false;
    }

    @Override
    public abstract NBTTag clone();

    public static NBTTag createTag(String name, NBTTypes type)
    {
        switch(type)
        {
            case END:
                return new NBTEndTag();
            case BYTE:
                return new NBTByteTag(name);
            case SHORT:
                return new NBTShortTag(name);
            case INT:
                return new NBTIntTag(name);
            case LONG:
                return new NBTLongTag(name);
            case FLOAT:
                return new NBTFloatTag(name);
            case DOUBLE:
                return new NBTDoubleTag(name);
            case BYTE_ARRAY:
                return new NBTByteArrayTag(name);
            case INT_ARRAY:
                return new NBTIntArrayTag(name);
            case STRING:
                return new NBTStringTag(name);
            case LIST:
                return new NBTListTag<NBTTag>(name);
            case COMPOUND:
                return new NBTCompoundTag(name);
        }
        return null;
    }

    public NBTTag setName(String name)
    {
        this.name = name;
        return this;
    }

    public static void writeNamedTag(NBTTag tag, DataOutput dos) throws IOException
    {
        dos.writeByte(tag.getID().getByteID());
        dos.writeUTF(tag.getName());
        tag.write(dos);
    }

    public static NBTTag readNamedTag(DataInput dis) throws IOException
    {
        NBTTypes type = NBTTypes.getFromID(dis.readByte());
        String name = dis.readUTF();
        NBTTag tag = createTag(name, type);
        tag.read(dis);
        return tag;
    }

}
