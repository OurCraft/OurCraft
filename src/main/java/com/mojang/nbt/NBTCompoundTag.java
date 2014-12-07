package com.mojang.nbt;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import com.google.gson.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mojang AB
 */
public class NBTCompoundTag extends NBTTag
{
    private Map<String, NBTTag> tags = new HashMap<String, NBTTag>();

    public NBTCompoundTag()
    {
        super("");
    }

    public NBTCompoundTag(String name)
    {
        super(name);
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        for(NBTTag tag : tags.values())
        {
            NBTTag.writeNamedTag(tag, dos);
        }
        dos.writeByte(NBTTypes.END.getByteID());
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        tags.clear();
        NBTTag tag;
        while((tag = NBTTag.readNamedTag(dis)).getID() != NBTTypes.END)
        {
            tags.put(tag.getName(), tag);
        }
    }

    public Collection<NBTTag> getAllTags()
    {
        return tags.values();
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.COMPOUND;
    }

    public void put(String name, NBTTag tag)
    {
        tags.put(name, tag.setName(name));
    }

    public void putByte(String name, byte value)
    {
        tags.put(name, new NBTByteTag(name, value));
    }

    public void putShort(String name, short value)
    {
        tags.put(name, new NBTShortTag(name, value));
    }

    public void putInt(String name, int value)
    {
        tags.put(name, new NBTIntTag(name, value));
    }

    public void putLong(String name, long value)
    {
        tags.put(name, new NBTLongTag(name, value));
    }

    public void putFloat(String name, float value)
    {
        tags.put(name, new NBTFloatTag(name, value));
    }

    public void putDouble(String name, double value)
    {
        tags.put(name, new NBTDoubleTag(name, value));
    }

    public void putString(String name, String value)
    {
        tags.put(name, new NBTStringTag(name, value));
    }

    public void putByteArray(String name, byte[] value)
    {
        tags.put(name, new NBTByteArrayTag(name, value));
    }

    public void putIntArray(String name, int[] value)
    {
        tags.put(name, new NBTIntArrayTag(name, value));
    }

    public void putCompound(String name, NBTCompoundTag value)
    {
        tags.put(name, value.setName(name));
    }

    public void putBoolean(String string, boolean val)
    {
        putByte(string, val ? (byte) 1 : 0);
    }

    public NBTTag get(String name)
    {
        return tags.get(name);
    }

    public boolean contains(String name)
    {
        return tags.containsKey(name);
    }

    public byte getByte(String name)
    {
        if(!tags.containsKey(name))
            return (byte) 0;
        return ((NBTByteTag) tags.get(name)).getData();
    }

    public short getShort(String name)
    {
        if(!tags.containsKey(name))
            return (short) 0;
        return ((NBTShortTag) tags.get(name)).getData();
    }

    public int getInt(String name)
    {
        if(!tags.containsKey(name))
            return (int) 0;
        return ((NBTIntTag) tags.get(name)).getData();
    }

    public long getLong(String name)
    {
        if(!tags.containsKey(name))
            return (long) 0;
        return ((NBTLongTag) tags.get(name)).getData();
    }

    public float getFloat(String name)
    {
        if(!tags.containsKey(name))
            return (float) 0;
        return ((NBTFloatTag) tags.get(name)).getData();
    }

    public double getDouble(String name)
    {
        if(!tags.containsKey(name))
            return (double) 0;
        return ((NBTDoubleTag) tags.get(name)).getData();
    }

    public String getString(String name)
    {
        if(!tags.containsKey(name))
            return "";
        return ((NBTStringTag) tags.get(name)).getData();
    }

    public byte[] getByteArray(String name)
    {
        if(!tags.containsKey(name))
            return new byte[0];
        return ((NBTByteArrayTag) tags.get(name)).getData();
    }

    public int[] getIntArray(String name)
    {
        if(!tags.containsKey(name))
            return new int[0];
        return ((NBTIntArrayTag) tags.get(name)).getData();
    }

    public NBTCompoundTag getCompound(String name)
    {
        if(!tags.containsKey(name))
            return new NBTCompoundTag(name);
        return (NBTCompoundTag) tags.get(name);
    }

    @SuppressWarnings("unchecked")
    public NBTListTag<? extends NBTTag> getList(String name)
    {
        if(!tags.containsKey(name))
            return new NBTListTag<NBTTag>(name);
        return (NBTListTag<? extends NBTTag>) tags.get(name);
    }

    public boolean getBoolean(String string)
    {
        return getByte(string) != 0;
    }

    @Override
    public String toString()
    {
        return "" + tags.size() + " entries";
    }

    public boolean isEmpty()
    {
        return tags.isEmpty();
    }

    @Override
    public NBTTag clone()
    {
        NBTCompoundTag tag = new NBTCompoundTag(getName());
        for(String key : tags.keySet())
        {
            tag.put(key, tags.get(key).clone());
        }
        return tag;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(super.equals(obj))
        {
            NBTCompoundTag o = (NBTCompoundTag) obj;
            return tags.entrySet().equals(o.tags.entrySet());
        }
        return false;
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject object = new JsonObject();
        for(Entry<String, NBTTag> tagEntry : tags.entrySet())
        {
            object.add(tagEntry.getKey(), tagEntry.getValue().toJson());
        }
        return object;
    }

}
