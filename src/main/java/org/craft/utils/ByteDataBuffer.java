package org.craft.utils;

import java.io.*;
import java.nio.*;

public class ByteDataBuffer implements Flushable, Closeable, DataInput, DataOutput
{

    private ByteArrayOutputStream out;
    private ByteOrder             order;
    private byte[]                buffer;
    private int                   index;

    public ByteDataBuffer()
    {
        this.out = new ByteArrayOutputStream();
        this.order = ByteOrder.BIG_ENDIAN;
    }

    public ByteDataBuffer(byte[] buffer)
    {
        this.buffer = buffer;
        this.order = ByteOrder.BIG_ENDIAN;
    }

    public ByteDataBuffer(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buff = new byte[65565];
        int i;
        while((i = inputStream.read(buff)) != -1)
        {
            out.write(buff, 0, i);
        }
        out.flush();
        out.close();
        buffer = out.toByteArray();
        this.order = ByteOrder.BIG_ENDIAN;
    }

    @Override
    public void close() throws IOException
    {
        if(out != null)
            out.close();
    }

    public void flush() throws IOException
    {
        out.flush();
    }

    public boolean isReadable() throws IOException
    {
        return readableBytes() > 0;
    }

    private int next()
    {
        return buffer[index++ ];
    }

    public int readableBytes() throws IOException
    {
        return buffer.length - index;
    }

    public boolean readBoolean() throws IOException
    {
        return readByte() == 1;
    }

    public byte readByte() throws IOException
    {
        return (byte) next();
    }

    public char readChar() throws IOException
    {
        if(order == ByteOrder.BIG_ENDIAN)
        {
            return (char) (next() << 8 | next() << 0);
        }
        else
        {
            return (char) (next() << 0 | next() << 8);
        }
    }

    public double readDouble() throws IOException
    {
        byte[] bytes = new byte[8];
        for(int i = 0; i < bytes.length; i++ )
            bytes[i] = readByte();
        return ByteBuffer.wrap(bytes).order(order).getDouble();
    }

    public float readFloat() throws IOException
    {
        byte[] bytes = new byte[4];
        for(int i = 0; i < bytes.length; i++ )
            bytes[i] = readByte();
        return ByteBuffer.wrap(bytes).order(order).getFloat();
    }

    @Override
    public void readFully(byte[] b) throws IOException
    {
        readFully(b, 0, b.length);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException
    {
        for(int i = off; i < len + off; i++ )
        {
            b[off + i] = (byte) next();
        }
    }

    public int readInt() throws IOException
    {
        if(order == ByteOrder.BIG_ENDIAN)
        {
            return next() << 24 | next() << 16 | next() << 8 | next() << 0;
        }
        else
        {
            return next() << 0 | next() << 8 | next() << 16 | next() << 24;
        }
    }

    @Override
    public String readLine() throws IOException
    {
        // TODO
        return "TEMPORARY, PLEASE FIX ME LATER";
    }

    public long readLong() throws IOException
    {
        byte[] bytes = new byte[8];
        for(int i = 0; i < bytes.length; i++ )
            bytes[i] = readByte();
        return ByteBuffer.wrap(bytes).order(order).getLong();
    }

    public short readShort() throws IOException
    {
        if(order == ByteOrder.BIG_ENDIAN)
        {
            return (short) (next() << 8 | next() << 0);
        }
        else
        {
            return (short) (next() << 0 | next() << 8);
        }
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

    @Override
    public int readUnsignedByte() throws IOException
    {
        return next() & 0xFF;
    }

    @Override
    public int readUnsignedShort() throws IOException
    {
        return readShort() & 0xFFFF;
    }

    @Override
    public String readUTF() throws IOException
    {
        return readString();
    }

    public void setOrder(ByteOrder order)
    {
        this.order = order;
    }

    @Override
    public int skipBytes(int n) throws IOException
    {
        return index += n;
    }

    public byte[] toBytes()
    {
        return out.toByteArray();
    }

    public void write(byte b) throws IOException
    {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        for(int i = off; i < off + len; i++ )
            writeByte(b[i]);
    }

    @Override
    public void write(int b) throws IOException
    {
        writeByte((byte) b);
    }

    public void writeBoolean(boolean b) throws IOException
    {
        out.write(b ? 1 : 0);
    }

    public void writeByte(byte b)
    {
        out.write(b);
    }

    @Override
    public void writeByte(int v) throws IOException
    {
        writeByte((byte) v);
    }

    @Override
    public void writeBytes(String s) throws IOException
    {
        writeString(s);
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

    @Override
    public void writeChar(int v) throws IOException
    {
        writeChar((char) v);
    }

    @Override
    public void writeChars(String s) throws IOException
    {
        writeString(s);
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
            out.write((byte) (l >> 56) & 0xFF);
            out.write((byte) (l >> 48) & 0xFF);
            out.write((byte) (l >> 40) & 0xFF);
            out.write((byte) (l >> 32) & 0xFF);
            out.write((byte) (l >> 24) & 0xFF);
            out.write((byte) (l >> 16) & 0xFF);
            out.write((byte) (l >> 8) & 0xFF);
            out.write((byte) (l >> 0) & 0xFF);
        }
    }

    @Override
    public void writeShort(int v) throws IOException
    {
        writeShort((short) v);
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

    public void writeString(String s) throws IOException
    {
        writeInt(s.length());
        for(char c : s.toCharArray())
        {
            writeChar(c);
        }
    }

    @Override
    public void writeUTF(String s) throws IOException
    {
        writeString(s);
    }

}
