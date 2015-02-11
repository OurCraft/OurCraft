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

    public void apply(int targetFramebuffer, Texture texture, OpenGLBuffer buffer, RenderEngine engine)
    {
        for(int i = 0; i < shaders.size(); i++ )
        {
            OpenGLBuffer renderBuffer = buffer;
            Texture colorTexture = texture;
            ShaderInfo info = shaders.get(i);
            if(i != 0)
            {
                engine.setProjectFromEntity(false);
                //                engine.setModelviewMatrix(new Matrix4().initIdentity());
                engine.setProjectionMatrix(new Matrix4().initOrthographic(0, colorTexture.getWidth(), colorTexture.getHeight(), 0, 0, 1));
            }
            engine.setCurrentShader(info.getShader());
            if(i == shaders.size() - 1)
            {
                glBindFramebuffer(GL_FRAMEBUFFER, targetFramebuffer);
            }
            else
            {
                FramebufferInfo frameBuffer = getFrameBuffer(info.getWidth(), info.getHeight());
                glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer.getID());
                renderBuffer = frameBuffer.getBuffer();
                colorTexture = frameBuffer.getColorBuffer();
            }
            glViewport(0, 0, colorTexture.getWidth(), colorTexture.getHeight());
            //            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            engine.bindTexture(colorTexture);
            engine.renderBuffer(renderBuffer);
        }
    }

    private FramebufferInfo getFrameBuffer(int width, int height)
    {
        if(width <= 0)
            width = OurCraft.getOurCraft().getDisplayWidth();
        if(height <= 0)
            height = OurCraft.getOurCraft().getDisplayHeight();
        for(FramebufferInfo info : framebuffers)
        {
            if(info.getWidth() == width && info.getHeight() == height)
                return info;
        }
        OpenGLBuffer buffer = new OpenGLBuffer();
        buffer.addVertex(Vertex.get(Vector3.get(0, 0, 0), Vector2.get(0, 0)));
        buffer.addVertex(Vertex.get(Vector3.get(width, 0, 0), Vector2.get(1, 0)));
        buffer.addVertex(Vertex.get(Vector3.get(width, height, 0), Vector2.get(1, 1)));
        buffer.addVertex(Vertex.get(Vector3.get(0, height, 0), Vector2.get(0, 1)));

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
        Texture colorBuffer = new Texture(width, height, null);

        int depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        int framebufferId = glGenFramebuffers();
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

        FramebufferInfo info = new FramebufferInfo(framebufferId, width, height, colorBuffer, buffer);
        framebuffers.add(info);
        return info;
    }
}
