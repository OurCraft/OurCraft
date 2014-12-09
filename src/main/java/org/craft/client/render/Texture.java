package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.nio.*;

import org.craft.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

public class Texture implements ITextureObject, IDisposable
{

    static
    {
        empty = new Texture(1, 1, (ByteBuffer) ByteBuffer.allocateDirect(4).put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255).flip());
    }
    public static final Texture empty;
    private int                 texID;
    private int                 width;
    private int                 height;
    private ByteBuffer          pixels;

    /**
     * Creates a texture with given width, height and pixel data
     */
    public Texture(int w, int h, ByteBuffer p)
    {
        this(w, h, p, GL_NEAREST);
    }

    public Texture(int w, int h, ByteBuffer pixels, int filter)
    {
        this.width = w;
        this.height = h;
        this.pixels = pixels;
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    /**
     * Returns corresponding OpenGL texture
     */
    public int getTextureID()
    {
        return texID;
    }

    @Override
    public void dispose()
    {
        glDeleteTextures(texID);
    }

    /**
     * Returns width of this texture (in pixels)
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Returns height of this texture (in pixels)
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Returns a buffer containing all pixels in RGBA format
     */
    public ByteBuffer getPixels()
    {
        return pixels;
    }

    public static Texture createColoredRect(int color)
    {
        return createColoredRect(1, 1, color);
    }

    public static Texture createColoredRect(int w, int h, int color)
    {
        ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color >> 0 & 0xFF;
        for(int y = 0; y < h; y++ )
        {
            for(int x = 0; x < w; x++ )
            {
                buffer.put((byte) r);
                buffer.put((byte) g);
                buffer.put((byte) b);
                buffer.put((byte) a);
            }
        }
        buffer.flip();
        Texture result = new Texture(w, h, buffer);
        return result;
    }
}
