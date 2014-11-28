package com.mojang.nbt;

import java.io.*;
import java.util.*;

import com.google.gson.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mojang AB
 */
public class NBTListTag<T extends NBTTag> extends NBTTag
{

    private List<T>  list = new ArrayList<T>();
    private NBTTypes type;

    public NBTListTag()
    {
        super("");
    }

    public NBTListTag(String name)
    {
        super(name);
    }

    public void write(DataOutput dos) throws IOException
    {
        if(list.size() > 0)
            type = list.get(0).getID();
        else
            type = NBTTypes.getFromID((byte) 1);

        dos.writeByte(type.getByteID());
        dos.writeInt(list.size());
        for(int i = 0; i < list.size(); i++ )
            list.get(i).write(dos);
    }

    @SuppressWarnings("unchecked")
    public void read(DataInput dis) throws IOException
    {
        type = NBTTypes.getFromID(dis.readByte());
        int size = dis.readInt();

        list = new ArrayList<T>();
        for(int i = 0; i < size; i++ )
        {
            NBTTag tag = NBTTag.createTag(null, type);
            tag.read(dis);
            list.add((T) tag);
        }
    }

    public NBTTypes getID()
    {
        return NBTTypes.LIST;
    }

    public String toString()
    {
        return "" + list.size() + " entries of type " + type.name();
    }

    public void add(T tag)
    {
        type = tag.getID();
        list.add(tag);
    }

    public T get(int index)
    {
        return list.get(index);
    }

    public int size()
    {
        return list.size();
    }

    @Override
    public NBTTag clone()
    {
        NBTListTag<T> res = new NBTListTag<T>(getName());
        res.type = type;
        for(T t : list)
        {
            @SuppressWarnings("unchecked")
            T copy = (T) t.clone();
            res.list.add(copy);
        }
        return res;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTListTag o = (NBTListTag) obj;
            if(type == o.type)
            {
                return list.equals(o.list);
            }
        }
        return false;
    }

    @Override
    public JsonElement toJson()
    {
        JsonArray array = new JsonArray();
        for(int i = 0; i < list.size(); i++ )
        {
            array.add(list.get(i).toJson());
        }
        return array;
    }
}
