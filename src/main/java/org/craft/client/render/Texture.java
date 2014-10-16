package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import org.craft.utils.*;
import org.lwjgl.opengl.*;

public class Texture implements ITextureObject, IDisposable
{

    private int texID;
    private int width;
    private int height;
    private ByteBuffer pixels;
    
    /**
     * Creates a texture with given width, height and pixel data
     */
    public Texture(int w, int h, ByteBuffer p)
    {
        this.width = w;
        this.height = h;
        this.pixels = p;
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public int getTextureID()
    {
        return texID;
    }

    public void dispose()
    {
        glDeleteTextures(texID);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
    
    public ByteBuffer getPixels()
    {
        return pixels;
    }
}
