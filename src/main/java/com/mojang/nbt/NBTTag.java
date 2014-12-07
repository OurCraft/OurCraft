package com.mojang.nbt;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.*;

import com.google.gson.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mojang AB
 */
public abstract class NBTTag implements Cloneable
{

    private String name;
    protected Gson gson;

    protected NBTTag(String name)
    {
        this.name = name;
        gson = new Gson();
    }

    public String getName()
    {
        return name;
    }

    public abstract void write(DataOutput dos) throws IOException;

    public abstract void read(DataInput dis) throws IOException;

    @Override
    public abstract String toString();

    public abstract JsonElement toJson();

    public abstract NBTTypes getID();

    @Override
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
        if(type == NBTTypes.END)
        {
            return new NBTEndTag();
        }
        String name = dis.readUTF();
        NBTTag tag = createTag(name, type);
        tag.read(dis);
        return tag;
    }

    public static NBTCompoundTag readCompoundFromJson(JsonObject object)
    {
        NBTCompoundTag compound = new NBTCompoundTag();
        Iterator<Entry<String, JsonElement>> it = object.entrySet().iterator();
        while(it.hasNext())
        {
            Entry<String, JsonElement> entry = it.next();
            if(entry.getValue().isJsonNull())
            {
                ;
            }
            else if(entry.getValue().isJsonObject())
            {
                compound.putCompound(entry.getKey(), readCompoundFromJson(entry.getValue().getAsJsonObject()));
            }
            else if(entry.getValue().isJsonPrimitive())
            {
                JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
                if(primitive.isBoolean())
                {
                    compound.putBoolean(entry.getKey(), primitive.getAsBoolean());
                }
                else if(primitive.isNumber()) // Tell me if you have a better way
                {
                    compound.putByte(entry.getKey(), primitive.getAsByte());
                    compound.putInt(entry.getKey(), primitive.getAsInt());
                    compound.putFloat(entry.getKey(), primitive.getAsFloat());
                    compound.putDouble(entry.getKey(), primitive.getAsDouble());
                    compound.putShort(entry.getKey(), primitive.getAsShort());
                    compound.putLong(entry.getKey(), primitive.getAsLong());
                }
                else if(primitive.isString())
                {
                    compound.putString(entry.getKey(), primitive.getAsString());
                }
            }
            else if(entry.getValue().isJsonArray())
            {
                JsonArray array = entry.getValue().getAsJsonArray();
                byte[] byteArray = new byte[array.size()];
                int[] intArray = new int[array.size()];

                for(int i = 0; i < array.size(); i++ )
                {
                    byteArray[i] = array.get(i).getAsByte();
                    intArray[i] = array.get(i).getAsInt();
                }

                compound.putByteArray(entry.getKey(), byteArray);
                compound.putIntArray(entry.getKey(), intArray);
            }
        }
        return compound;
    }

    public static void writeCompoundToFile(File file, NBTCompoundTag tag) throws IOException
    {
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(file))));
        writeNamedTag(tag, dos);
        dos.flush();
        dos.close();
    }

    public static NBTCompoundTag readCompoundFromFile(File worldDataFile) throws IOException
    {
        GZIPInputStream input = new GZIPInputStream(new FileInputStream(worldDataFile));
        DataInputStream in = new DataInputStream(input);
        NBTTag tag = readNamedTag(in);
        in.close();
        if(!(tag instanceof NBTCompoundTag))
        {
            throw new IllegalArgumentException("Given file does not contain a NBT compound tag, found instance of " + tag.getClass());
        }
        return (NBTCompoundTag) tag;
    }

}
