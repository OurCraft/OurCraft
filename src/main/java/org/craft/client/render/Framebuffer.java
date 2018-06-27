package org.craft.client.render;

import static org.craft.client.OurCraft.printIfGLError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16;
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
        System.out.println(">> "+w+" / "+h);
        colorBuffer = new Texture(w, h, null);
        printIfGLError("post create color texture");

        depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_STENCIL, w, h);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        printIfGLError("post create depth-stencil");

        framebufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorBuffer.getTextureID(), 0);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
        glDrawBuffers((IntBuffer) BufferUtils.createIntBuffer(1).put(GL_COLOR_ATTACHMENT0).flip());
        printIfGLError("post create framebuffer");

        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status != GL_FRAMEBUFFER_COMPLETE)
        {
            Log.fatal("Framebuffer could not be created, status code: " + Integer.toHexString(status));
        }
        printIfGLError("check framebuffer");
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
