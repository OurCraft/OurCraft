package org.craft.nbt;

import java.io.*;

import com.google.gson.*;

/**
 * Inspired by NBT classes given by Mojang AB <a href="https://mojang.com/2012/02/new-minecraft-map-format-anvil/">here</a>
 * <br/>Following the <a href="http://web.archive.org/web/20110723210920/http://www.minecraft.net/docs/NBT.txt">specifications created by Markus 'notch' Personn </a>
 * @author Mostly Mojang AB
 */
public class NBTEndTag extends NBTTag
{

    public NBTEndTag()
    {
        super("");
    }

    @Override
    public void write(DataOutput dos) throws IOException
    {
        ;
    }

    @Override
    public void read(DataInput dis) throws IOException
    {
        ;
    }

    @Override
    public String toString()
    {
        return "nil";
    }

    @Override
    public NBTTypes getID()
    {
        return NBTTypes.END;
    }

    @Override
    public NBTTag clone()
    {
        return new NBTEndTag();
    }

    @Override
    public JsonElement toJson()
    {
        return JsonNull.INSTANCE;
    }
}
