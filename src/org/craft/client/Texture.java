package org.craft.client;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import org.lwjgl.opengl.*;

public class Texture
{

    private int texID;

    public Texture(int w, int h, ByteBuffer pixels)
    {
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);

        glClampColor(GL_TEXTURE_2D, GL_FALSE);
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
}
