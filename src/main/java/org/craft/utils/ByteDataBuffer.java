package org.craft.utils;

import java.io.*;
import java.nio.*;

public class ByteDataBuffer implements Flushable, Closeable
{

    private ByteArrayOutputStream out;
    private ByteOrder             order;
    private InputStream           in;

    public ByteDataBuffer(byte[] buffer)
    {
        this.in = new ByteArrayInputStream(buffer);
        this.order = ByteOrder.BIG_ENDIAN;
    }

    public ByteDataBuffer()
    {
        this.out = new ByteArrayOutputStream();
        this.order = ByteOrder.BIG_ENDIAN;
    }

    public ByteDataBuffer(InputStream inputStream)
    {
        this.in = inputStream;
        this.order = ByteOrder.BIG_ENDIAN;
    }

    public void setOrder(ByteOrder order)
    {
        this.order = order;
    }

    public byte[] toBytes()
    {
        return out.toByteArray();
    }

    public short readShort() throws IOException
    {
        if(order == ByteOrder.BIG_ENDIAN)
        {
            return (short) (in.read() << 8 | in.read() << 0);
        }
        else
        {
            return (short) (in.read() << 0 | in.read() << 8);
        }
    }

    public void writeShort(short c)
    {
        if(order == ByteOrder.BIG_ENDIAN)
        {
            out.write(c >> 8 & 0xFF);
            out.write(c >> 0 & 0xFF);
        }
        else if(order == ByteOrder.LITTLE_ENDIAN)
        {
            out.write(c >> 0 & 0xFF);
            out.write(c >> 8 & 0xFF);
        }
    }

    public void writeByte(byte b)
    {
        out.write(b);
    }

    public String readString() throws IOException
    {
        char[] chars = new char[readInt()];
        for(int i = 0; i < chars.length; i++ )
        {
            chars[i] = readChar();
        }
        return new String(chars);
    }

    public long readLong() throws IOException
    {
        long result = 0;
        for(int i = 0; i < 8; i++ )
        {
            result |= (short) (((readByte() & 0xFF) << 8 * (order == ByteOrder.BIG_ENDIAN ? 8 - i - 1 : i)));
        }
        return result;
    }

    public float readFloat() throws IOException
    {
        byte[] bytes = new byte[4];
        for(int i = 0; i < bytes.length; i++ )
            bytes[i] = readByte();
        return ByteBuffer.wrap(bytes).order(order).getFloat();
    }

    public double readDouble() throws IOException
    {
        byte[] bytes = new byte[8];
        for(int i = 0; i < bytes.length; i++ )
            bytes[i] = readByte();
        return ByteBuffer.wrap(bytes).order(order).getDouble();
    }

    public void writeLong(long l) throws IOException
    {
        if(order == ByteOrder.LITTLE_ENDIAN)
        {
            out.write((byte) (l & 0xFF));
            out.write((byte) (l >> 8) & 0xFF);
            out.write((byte) (l >> 16) & 0xFF);
            out.write((byte) (l >> 24) & 0xFF);
            out.write((byte) (l >> 32) & 0xFF);
            out.write((byte) (l >> 40) & 0xFF);
            out.write((byte) (l >> 48) & 0xFF);
            out.write((byte) (l >> 56) & 0xFF);
        }
        else if(order == ByteOrder.BIG_ENDIAN)
        {
            out.write((byte) (l >> 56 & 0xFF));
            out.write((byte) (l >> 48) & 0xFF);
            out.write((byte) (l >> 40) & 0xFF);
            out.write((byte) (l >> 32) & 0xFF);
            out.write((byte) (l >> 24) & 0xFF);
            out.write((byte) (l >> 16) & 0xFF);
            out.write((byte) (l >> 8) & 0xFF);
            out.write((byte) (l >> 0) & 0xFF);
        }
    }

    public void writeDouble(double d) throws IOException
    {
        long bits = Double.doubleToLongBits(d);
        if(order == ByteOrder.LITTLE_ENDIAN)
        {
            out.write((byte) (bits & 0xFF));
            out.write((byte) (bits >> 8) & 0xFF);
            out.write((byte) (bits >> 16) & 0xFF);
            out.write((byte) (bits >> 24) & 0xFF);
            out.write((byte) (bits >> 32) & 0xFF);
            out.write((byte) (bits >> 40) & 0xFF);
            out.write((byte) (bits >> 48) & 0xFF);
            out.write((byte) (bits >> 56) & 0xFF);
        }
        else if(order == ByteOrder.BIG_ENDIAN)
        {
            out.write((byte) (bits >> 56 & 0xFF));
            out.write((byte) (bits >> 48) & 0xFF);
            out.write((byte) (bits >> 40) & 0xFF);
            out.write((byte) (bits >> 32) & 0xFF);
            out.write((byte) (bits >> 24) & 0xFF);
            out.write((byte) (bits >> 16) & 0xFF);
            out.write((byte) (bits >> 8) & 0xFF);
            out.write((byte) (bits >> 0) & 0xFF);
        }
    }

    public void writeFloat(float f) throws IOException
    {
        int bits = Float.floatToIntBits(f);
        if(order == ByteOrder.LITTLE_ENDIAN)
        {
            out.write(bits & 0xFF);
            out.write((bits >> 8) & 0xFF);
            out.write((bits >> 16) & 0xFF);
            out.write((bits >> 24) & 0xFF);
        }
        else if(order == ByteOrder.BIG_ENDIAN)
        {
            out.write((bits >> 24) & 0xFF);
            out.write((bits >> 16) & 0xFF);
            out.write((bits >> 8) & 0xFF);
            out.write(bits & 0xFF);
        }
    }

    public void writeString(String s) throws IOException
    {
        writeInt(s.length());
        for(char c : s.toCharArray())
        {
            writeChar(c);
        }
    }

    public char readChar() throws IOException
    {
        if(order == ByteOrder.BIG_ENDIAN)
        {
            return (char) (in.read() << 8 | in.read() << 0);
        }
        else
        {
            return (char) (in.read() << 0 | in.read() << 8);
        }
    }

    public void writeChar(char c)
    {
        if(order == ByteOrder.BIG_ENDIAN)
        {
            out.write(c >> 8 & 0xFF);
            out.write(c >> 0 & 0xFF);
        }
        else if(order == ByteOrder.LITTLE_ENDIAN)
        {
            out.write(c >> 0 & 0xFF);
            out.write(c >> 8 & 0xFF);
        }
    }

    public void writeBoolean(boolean b) throws IOException
    {
        out.write(b ? 1 : 0);
    }

    public void write(byte b) throws IOException
    {
        out.write(b);
    }

    public boolean readBoolean() throws IOException
    {
        return readByte() == 1;
    }

    public byte readByte() throws IOException
    {
        return (byte) in.read();
    }

    public int readInt() throws IOException
    {
        if(order == ByteOrder.BIG_ENDIAN)
        {
            return in.read() << 24 | in.read() << 16 | in.read() << 8 | in.read() << 0;
        }
        else
        {
            return in.read() << 0 | in.read() << 8 | in.read() << 16 | in.read() << 24;
        }
    }

    public void writeInt(int i) throws IOException
    {
        if(order == ByteOrder.BIG_ENDIAN)
        {
            out.write(i >> 24 & 0xFF);
            out.write(i >> 16 & 0xFF);
            out.write(i >> 8 & 0xFF);
            out.write(i >> 0 & 0xFF);
        }
        else if(order == ByteOrder.LITTLE_ENDIAN)
        {
            out.write(i >> 0 & 0xFF);
            out.write(i >> 8 & 0xFF);
            out.write(i >> 16 & 0xFF);
            out.write(i >> 24 & 0xFF);
        }
    }

    public void flush() throws IOException
    {
        out.flush();
    }

    @Override
    public void close() throws IOException
    {
        if(in != null)
            in.close();
        if(out != null)
            out.close();
    }

    public boolean isReadable() throws IOException
    {
        return in != null && in.available() > 0;
    }

}
