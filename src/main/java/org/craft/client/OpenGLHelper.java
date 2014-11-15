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
import org.lwjgl.opengl.*;

public class OpenGLHelper
{

    /**
     * Loads a OpenGL Texture from given image
     */
    public static Texture loadTexture(BufferedImage img)
    {
        return loadTexture(img, GL_NEAREST);
    }

    public static Texture loadTexture(BufferedImage img, int filter)
    {
        int w = img.getWidth();
        int h = img.getHeight();
        ByteBuffer pixelBuf = ImageUtils.getPixels(img);
        return new Texture(w, h, pixelBuf, filter);
    }

    /**
     * Loads given texture
     */
    public static Texture loadTexture(AbstractResource resource) throws IOException
    {
        BufferedImage img = ImageUtils.loadImage(resource);
        return loadTexture(img);
    }

    private static HashMap<Integer, String> capNamesMap = new HashMap<Integer, String>();

    /**
     * Loads all GL_* fields with their respective values (used mostly for crash reports)
     */
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
    }

    /**
     * Loads all GL_* fields with their respective values from given class (used mostly for crash reports)
     */
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

    /**
     * Returns OpenGL version
     */
    public static String getOpenGLVersion()
    {
        String version = glGetString(GL_VERSION);
        
        // Remove Driver Info
        if(version.indexOf(' ') != -1)
            version = version.substring(0, version.indexOf(' '));
        return version;
    }

    /**
     * Returns OpenGL vendor
     */
    public static String getOpenGLVendor()
    {
        return glGetString(GL_VENDOR);
    }
    
    /**
     * Returns OpenGL renderer info
     */
    public static String getOpenGLRendererInfo()
    {
        return glGetString(GL_RENDERER);
    }

    /**
     * Returns OpenGL capability name
     */
    public static String getCapName(int cap)
    {
        if(!capNamesMap.containsKey(cap))
            return "" + cap;
        return capNamesMap.get(cap);
    }

}
