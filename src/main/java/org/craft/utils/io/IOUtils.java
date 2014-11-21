package org.craft.utils.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public final class IOUtils
{

    /**
     */
    public static OutputStream copy(InputStream is, OutputStream os) throws IOException
    {
        int i = 0;
        byte[] buffer = new byte[65565];
        while((i = is.read(buffer, 0, buffer.length)) != -1)
        {
            os.write(buffer, 0, i);
        }
        return os;
    }

    public static OutputStream copy(InputStream in, String output) throws FileNotFoundException, IOException
    {
        return copy(in, new BufferedOutputStream(new FileOutputStream(output)));
    }

    public static String readString(InputStream in, String charset) throws UnsupportedEncodingException, IOException
    {
        return new String(read(in), charset);
    }

    public static byte[] read(InputStream in) throws IOException
    {
        byte[] buffer = new byte[65565];
        ByteArrayOutputStream ous = new ByteArrayOutputStream(buffer.length);
        int i = 0;
        while((i = in.read(buffer, 0, buffer.length)) != -1)
        {
            ous.write(buffer, 0, i);
        }
        ous.close();
        return ous.toByteArray();
    }

    public static void deleteFolderContents(File folder)
    {
        File[] files = folder.listFiles();
        if(files != null)
            for(File f : files)
            {
                if(f.isDirectory())
                {
                    deleteFolderContents(f);
                    f.delete();
                }
                else
                    f.delete();
            }
    }

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
