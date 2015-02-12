package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import org.craft.client.*;
import org.craft.client.render.texture.*;
import org.craft.utils.*;
import org.lwjgl.*;

public class Framebuffer implements IBindable
{

    private int     framebufferId;
    private int     depthBuffer;
    private Texture colorBuffer;

    public Framebuffer(int w, int h)
    {
        colorBuffer = new Texture(w, h, null);

        depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, w, h);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        framebufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorBuffer.getTextureID(), 0);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
        glDrawBuffers((IntBuffer) BufferUtils.createIntBuffer(2).put(GL_COLOR_ATTACHMENT0).put(GL_NONE).flip());

        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status != GL_FRAMEBUFFER_COMPLETE)
        {
            Log.fatal("Framebuffer could not be created, status code: " + status);
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public Texture getColorBuffer()
    {
        return colorBuffer;
    }

    public int getDepthBuffer()
    {
        return depthBuffer;
    }

    public int getID()
    {
        return framebufferId;
    }

    public void bind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
    }

}
