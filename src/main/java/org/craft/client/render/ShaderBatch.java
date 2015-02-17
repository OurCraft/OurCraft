package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.client.*;
import org.craft.client.render.texture.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.lwjgl.*;

public class ShaderBatch
{

    private List<ShaderInfo>      shaders;
    private List<FramebufferInfo> framebuffers;

    public ShaderBatch(Shader... shaders)
    {
        framebuffers = Lists.newArrayList();
        this.shaders = Lists.newArrayList();
        for(Shader shader : shaders)
        {
            ShaderInfo info = shader.getInfos();
            this.shaders.add(info);
        }
    }

    public ShaderBatch(ShaderInfo... shaders)
    {
        this(Lists.newArrayList(shaders));
    }

    public ShaderBatch(List<ShaderInfo> shaders)
    {
        framebuffers = Lists.newArrayList();
        this.shaders = shaders;
    }

    public ShaderBatch add(Shader info)
    {
        shaders.add(info.getInfos());
        return this;
    }

    public ShaderBatch add(ShaderInfo info)
    {
        shaders.add(info);
        return this;
    }

    public ShaderBatch remove(Shader info)
    {
        shaders.remove(info.getInfos());
        return this;
    }

    public ShaderBatch remove(ShaderInfo info)
    {
        shaders.remove(info);
        return this;
    }

    public void apply(int targetFramebuffer, Texture texture, OpenGLBuffer buffer, RenderEngine engine)
    {
        Texture renderTexture = texture;
        for(int i = 0; i < shaders.size(); i++ )
        {
            engine.pushState();
            ShaderInfo info = shaders.get(i);

            engine.setCurrentShader(info.getShader());
            engine.bindTexture(renderTexture, 0);
            if(i == shaders.size() - 1)
            {
                glBindFramebuffer(GL_FRAMEBUFFER, targetFramebuffer);
                engine.switchToOrtho();
                engine.flushBuffer(buffer, GL_TRIANGLES);
                glBindFramebuffer(GL_FRAMEBUFFER, 0);
            }
            else
            {
                FramebufferInfo framebuffer = null;
                framebuffer = getFrameBuffer(info, info.getWidth(), info.getHeight());
                glBindFramebuffer(GL_FRAMEBUFFER, framebuffer.getID());
                glClearColor(0, 0, 0, 0);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                OurCraft.printIfGLError("after clearing framebuffer in ShaderBatch");
                engine.switchToOrtho(framebuffer.getWidth(), framebuffer.getHeight());
                engine.flushBuffer(framebuffer.getBuffer(), GL_TRIANGLES);
                renderTexture = framebuffer.getColorBuffer();

            }
            engine.popState();
        }
    }

    FramebufferInfo getFrameBuffer(ShaderInfo shader, int width, int height)
    {
        return getFrameBuffer(shader, width, height, GL_NEAREST);
    }

    FramebufferInfo getFrameBuffer(ShaderInfo shader, int width, int height, int filter)
    {
        if(width == -1)
            width = OurCraft.getOurCraft().getDisplayWidth();
        if(height == -1)
            height = OurCraft.getOurCraft().getDisplayHeight();
        for(FramebufferInfo info : framebuffers)
        {
            if(info.getShader() == shader && info.getWidth() == width && info.getHeight() == height)
                return info;
        }
        OpenGLBuffer buffer = new OpenGLBuffer();
        buffer.addVertex(Vertex.get(Vector3.get(0, 0, 0), Vector2.get(0, 1)));
        buffer.addVertex(Vertex.get(Vector3.get(width, 0, 0), Vector2.get(1, 1)));
        buffer.addVertex(Vertex.get(Vector3.get(width, height, 0), Vector2.get(1, 0)));
        buffer.addVertex(Vertex.get(Vector3.get(0, height, 0), Vector2.get(0, 0)));

        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);

        buffer.addIndex(2);
        buffer.addIndex(3);
        buffer.addIndex(0);
        buffer.upload();
        buffer.clearAndDisposeVertices();

        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        int depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        int framebufferId = glGenFramebuffers();
        Texture colorBuffer = new Texture(width, height, null, filter);
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

        FramebufferInfo info = new FramebufferInfo(framebufferId, width, height, colorBuffer, buffer, shader);
        framebuffers.add(info);
        return info;
    }

    public void clear()
    {
        shaders.clear();
        framebuffers.clear();
    }
}
