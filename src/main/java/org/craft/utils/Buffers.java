package org.craft.utils;

import java.nio.*;

import org.lwjgl.*;

public class Buffers
{

    public static FloatBuffer createFloatBuffer(int size)
    {
        return BufferUtils.createFloatBuffer(size);
    }

    public static IntBuffer createIntBuffer(int size)
    {
        return BufferUtils.createIntBuffer(size);
    }

    public static ByteBuffer createByteBuffer(int size)
    {
        return BufferUtils.createByteBuffer(size);
    }

    public static IntBuffer createFlippedIntBuffer(int... indices)
    {
        IntBuffer buffer = createIntBuffer(indices.length);
        for(int i = 0; i < indices.length; i++ )
            buffer.put(indices[i]);
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(IBufferWritable... v)
    {
        int size = 0;
        for(int i = 0; i < v.length; i++ )
            size += v[i].getSize();
        FloatBuffer buffer = createFloatBuffer(size);
        for(int i = 0; i < v.length; i++ )
            v[i].write(buffer);
        buffer.flip();
        buffer.rewind();
        return buffer;
    }

    public static ByteBuffer createFlippedByteBuffer(byte... bufferData)
    {
        ByteBuffer buf = createByteBuffer(bufferData.length);
        for(int i = 0; i < bufferData.length; i++ )
            buf.put(bufferData[i]);
        buf.flip();
        return buf;
    }

    public static FloatBuffer createFlippedFloatBuffer(float... data)
    {
        FloatBuffer buf = createFloatBuffer(data.length);
        for(int i = 0; i < data.length; i++ )
            buf.put(data[i]);
        buf.flip();
        return buf;
    }

}
