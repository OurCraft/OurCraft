package com.mojang.nbt;

public enum NBTTypes
{
    END((byte) 0),
    BYTE((byte) 1),
    SHORT((byte) 2),
    INT((byte) 3),
    LONG((byte) 4),
    FLOAT((byte) 5),
    DOUBLE((byte) 6),
    BYTE_ARRAY((byte) 7),
    STRING((byte) 8),
    LIST((byte) 9),
    COMPOUND((byte) 10),
    INT_ARRAY((byte) 11);

    private byte id;

    private NBTTypes(byte id)
    {
        this.id = id;
    }

    public static NBTTypes getFromID(byte id)
    {
        for(NBTTypes t : values())
            if(t.id == id)
                return t;
        return null;
    }

    public byte getByteID()
    {
        return id;
    }

}
