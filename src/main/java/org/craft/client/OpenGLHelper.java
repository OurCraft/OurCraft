package org.craft.client;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.*;
import java.util.*;

import org.craft.client.render.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

public class OpenGLHelper
{

    /**
     * Loads a OpenGL Texture from given image
     */
    public static Texture loadTexture(BufferedImage img)
    {
        int w = img.getWidth();
        int h = img.getHeight();
        int[] pixels = img.getRGB(0, 0, w, h, null, 0, w);
        ByteBuffer pixelBuf = BufferUtils.createByteBuffer(pixels.length * 4);
        for(int y = 0; y < h; y++ )
        {
            for(int x = 0; x < w; x++ )
            {
                int color = pixels[x + y * w];
                int a = color >> 24 & 0xFF;
                int r = color >> 16 & 0xFF;
                int g = color >> 8 & 0xFF;
                int b = color >> 0 & 0xFF;
                pixelBuf.put((byte) r);
                pixelBuf.put((byte) g);
                pixelBuf.put((byte) b);
                pixelBuf.put((byte) a);
            }
        }
        pixelBuf.flip();
        return new Texture(w, h, pixelBuf);
    }

    public static Texture loadTexture(AbstractResource resource) throws IOException
    {
        BufferedImage img = ImageUtils.loadImage(resource);
        return loadTexture(img);
    }

    private static HashMap<Integer, String> capNamesMap = new HashMap<Integer, String>();

    public static void loadCapNames()
    {
        loadCapNames(GL11.class);
        loadCapNames(GL12.class);
        loadCapNames(GL13.class);
        loadCapNames(GL14.class);
        loadCapNames(GL15.class);
        loadCapNames(GL20.class);
        loadCapNames(GL21.class);
        loadCapNames(GL30.class);
        loadCapNames(GL31.class);
        loadCapNames(GL32.class);
        loadCapNames(GL33.class);
    }

    private static void loadCapNames(Class<?> glClass)
    {
        Field[] fields = glClass.getFields();
        for(Field field : fields)
        {
            if(Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
            {
                try
                {
                    if(field.getType() == Integer.TYPE)
                    {
                        int value = (Integer) field.get(null);
                        if(!capNamesMap.containsKey(value))
                        {
                            capNamesMap.put(value, field.getName());
                        }
                    }
                }
                catch(IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch(IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getOpenGLVersion()
    {
        return glGetString(GL_VERSION);
    }

    public static String getOpenGLVendor()
    {
        return glGetString(GL_VENDOR);
    }

    public static String getCapName(int cap)
    {
        if(!capNamesMap.containsKey(cap))
            return "" + cap;
        return capNamesMap.get(cap);
    }
}
