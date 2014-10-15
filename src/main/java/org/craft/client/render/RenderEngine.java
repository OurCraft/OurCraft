package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;
import java.util.*;

import org.craft.client.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

public class RenderEngine implements IDisposable
{

    private HashMap<ResourceLocation, ITextureObject> texturesLocs;
    private Entity                                    renderViewEntity;
    private Matrix4                                   projection3dMatrix;
    private Shader                                    currentShader;
    private Shader                                    basicShader;
    private Matrix4                                   projectionHud;
    private Matrix4                                   modelMatrix;
    private Matrix4                                   projection;
    private boolean                                   projectFromEntity;
    private ResourceLoader                            loader;
    private int                                       framebufferId;
    private OpenGLBuffer                              renderBuffer;
    private int                                       depthBuffer;
    private Texture                                   colorBuffer;
    private Shader                                    postRenderShader;
    private RenderState                               renderState;
    private Stack<RenderState>                        renderStatesStack;
    private Matrix4                                   translationMatrix;
    private Frustum                                   frustum;
    private float                                     fov;
    private float                                     ratio;
    private float                                     nearDist;
    private float                                     farDist;

    public RenderEngine(ResourceLoader loader) throws Exception
    {
        renderState = new RenderState();
        this.loader = loader;
        texturesLocs = new HashMap<ResourceLocation, ITextureObject>();
        projectFromEntity = true;
        modelMatrix = new Matrix4().initIdentity();
        fov = (float) Math.toRadians(90);
        ratio = 16f / 9f;
        nearDist = 0.01f;
        farDist = 100f;
        projection3dMatrix = new Matrix4().initPerspective(fov, ratio, nearDist, farDist);
        projection = projection3dMatrix;
        projectionHud = new Matrix4().initOrthographic(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        loadShaders();
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

        colorBuffer = new Texture(OurCraft.getOurCraft().getDisplayWidth(), OurCraft.getOurCraft().getDisplayHeight(), null);

        depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, OurCraft.getOurCraft().getDisplayWidth(), OurCraft.getOurCraft().getDisplayHeight());
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

        renderBuffer = new OpenGLBuffer();

        renderBuffer.addVertex(Vertex.get(Vector3.get(0, 0, 0), Vector2.get(0, 1)));
        renderBuffer.addVertex(Vertex.get(Vector3.get(Display.getWidth(), 0, 0), Vector2.get(1, 1)));
        renderBuffer.addVertex(Vertex.get(Vector3.get(Display.getWidth(), Display.getHeight(), 0), Vector2.get(1, 0)));
        renderBuffer.addVertex(Vertex.get(Vector3.get(0, Display.getHeight(), 0), Vector2.get(0, 0)));

        renderBuffer.addIndex(0);
        renderBuffer.addIndex(1);
        renderBuffer.addIndex(2);

        renderBuffer.addIndex(2);
        renderBuffer.addIndex(3);
        renderBuffer.addIndex(0);
        renderBuffer.upload();
        renderBuffer.clearAndDisposeVertices();

        this.frustum = new Frustum();
    }

    /**
     * Renders a buffer with given texture
     */
    public void renderBuffer(OpenGLBuffer buffer, ITextureObject texture)
    {
        bindTexture(texture);
        renderBuffer(buffer);
    }

    /**
     * Renders a buffer
     */
    public void renderBuffer(OpenGLBuffer buffer)
    {
        renderBuffer(buffer, GL_TRIANGLES);
    }

    /**
     * Renders a buffer with given mode
     */
    public void renderBuffer(OpenGLBuffer buffer, int mode)
    {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, buffer.getVboID());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 12);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 20);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer.getIboID());
        glDrawElements(mode, buffer.getIndicesCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
    }

    public Matrix4 getProjectedViewMatrix()
    {
        if(renderViewEntity != null && shouldProjectFromEntity())
        {
            if(translationMatrix == null)
                translationMatrix = new Matrix4();
            Quaternion camRot = renderViewEntity.getQuaternionRotation();
            Vector3 camPos = Vector3.get(-renderViewEntity.posX - 0.5f, -renderViewEntity.posY - renderViewEntity.getEyeOffset(), -renderViewEntity.posZ - 0.5f);
            frustum.update(fov, ratio, nearDist, farDist, camPos, camRot.getForward(), camRot.getUp());
            camPos.dispose();
            return projection.mul(camRot.conjugate().toRotationMatrix().mul(translationMatrix.initTranslation(-renderViewEntity.posX - 0.5f, -renderViewEntity.posY - renderViewEntity.getEyeOffset(), -renderViewEntity.posZ - 0.5f)));
        }
        return projection;
    }

    public void setRenderViewEntity(Entity e)
    {
        this.renderViewEntity = e;
    }

    public Entity getRenderViewEntity()
    {
        return renderViewEntity;
    }

    public void renderSplashScreen()
    {
        OpenGLBuffer buffer = new OpenGLBuffer();
        buffer.addVertex(Vertex.get(Vector3.get(0, 0, 0), Vector2.get(0, 0)));
        buffer.addVertex(Vertex.get(Vector3.get(OurCraft.getOurCraft().getDisplayWidth(), 0, 0), Vector2.get(1, 0)));
        buffer.addVertex(Vertex.get(Vector3.get(OurCraft.getOurCraft().getDisplayWidth(), OurCraft.getOurCraft().getDisplayHeight(), 0), Vector2.get(1, 1)));
        buffer.addVertex(Vertex.get(Vector3.get(0, OurCraft.getOurCraft().getDisplayHeight(), 0), Vector2.get(0, 1)));

        buffer.addIndex(0);
        buffer.addIndex(1);
        buffer.addIndex(2);

        buffer.addIndex(2);
        buffer.addIndex(3);
        buffer.addIndex(0);
        buffer.upload();
        renderBuffer(buffer, OpenGLHelper.loadTexture(ImageUtils.getFromClasspath("/assets/ourcraft/textures/loadingScreen.png")));
        buffer.dispose();
    }

    public void setModelviewMatrix(Matrix4 modelMatrix)
    {
        this.modelMatrix = modelMatrix;
        updateOpenGL();
    }

    public Matrix4 getModelviewMatrix()
    {
        return modelMatrix;
    }

    public void updateOpenGL()
    {
        currentShader.bind();
        currentShader.setUniform("modelview", this.modelMatrix);
        currentShader.setUniform("projection", getProjectedViewMatrix());
    }

    public void setCurrentShader(Shader shader)
    {
        this.currentShader = shader;
        updateOpenGL();
    }

    public Shader getCurrentShader()
    {
        return currentShader;
    }

    public void setProjectionMatrix(Matrix4 projection)
    {
        this.projection = projection;
        updateOpenGL();
    }

    public Matrix4 getProjectionMatrix()
    {
        return projection;
    }

    public void switchToOrtho()
    {
        projectFromEntity = false;
        setProjectionMatrix(projectionHud);
    }

    public void switchToPerspective()
    {
        projectFromEntity = true;
        setProjectionMatrix(projection3dMatrix);
    }

    public boolean shouldProjectFromEntity()
    {
        return projectFromEntity;
    }

    public void setProjectFromEntity(boolean flag)
    {
        this.projectFromEntity = flag;
    }

    public void setBlendFunc(int blendSrc, int blendDst)
    {
        renderState.setBlendFunc(blendSrc, blendDst);
        glBlendFunc(blendSrc, blendDst);
    }

    /**
     * Binds given texture object
     */
    public void bindTexture(ITextureObject object)
    {
        bindTexture(object, 0);
    }

    /**
     * Binds given texture to given texture unit slot
     */
    public void bindTexture(ITextureObject object, int slot)
    {
        if(object != null)
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
            object.bind();
        }
    }

    /**
     * Binds given texture to given texture unit slot
     */
    public void bindTexture(int texId, int slot)
    {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, texId);
    }

    /**
     * Binds a texture from given ResourceLocation<br/>
     * If the ResourceLocation doesn't have any texture bound to it, this method will try to bind one corresponding.<br/>
     * Unbinds current texture if fails.
     */
    public void bindLocation(ResourceLocation loc)
    {
        if(loc == null)
        {
            bindTexture(0, 0);
        }
        else if(!texturesLocs.containsKey(loc))
        {
            bindTexture(0, 0);
            try
            {
                texturesLocs.put(loc, OpenGLHelper.loadTexture(loader.getResource(loc)));
                bindLocation(loc);
                Log.debug("Unknown texture: /" + loc.getFullPath() + " charging it");
            }
            catch(Exception e)
            {
                e.printStackTrace();
                texturesLocs.put(loc, null);
            }
        }
        else
        {
            ITextureObject texObject = texturesLocs.get(loc);
            if(texObject != null)
                texObject.bind();
            else
                bindTexture(0, 0);
        }
    }

    /**
     * Bind a texture object to a ResourceLocation
     */
    public void registerLocation(ResourceLocation loc, ITextureObject object)
    {
        texturesLocs.put(loc, object);
    }

    /**
     * Disposes of current shader and textures bound to ResourceLocations
     */
    public void dispose()
    {
        for(ITextureObject o : texturesLocs.values())
            if(o instanceof IDisposable)
                ((IDisposable) o).dispose();
        currentShader.dispose();
    }

    /**
     * Reload all textures bound to a ResourceLocation.<br/>
     * If a texture can't be found, it will be discarded
     */
    public void reloadLocations() throws Exception
    {
        Iterator<ResourceLocation> it = texturesLocs.keySet().iterator();
        while(it.hasNext())
        {
            ResourceLocation key = it.next();
            try
            {
                texturesLocs.put(key, OpenGLHelper.loadTexture(loader.getResource(key)));
            }
            catch(Exception e)
            {
                texturesLocs.put(key, null);
                Log.error("Could not reload texture at /" + key.getFullPath() + " because of: " + e.getClass().getCanonicalName() + " " + e.getLocalizedMessage());
            }
        }
        RenderBlocks.createBlockMap(this);
        RenderItems.createItemMap(this);
    }

    /**
     * Loads all required shaders
     */
    public void loadShaders() throws Exception
    {
        basicShader = new Shader(new String(loader.getResource(new ResourceLocation("ourcraft/shaders", "base.vsh")).getData(), "UTF-8"), new String(loader.getResource(new ResourceLocation("ourcraft/shaders", "base.fsh")).getData(), "UTF-8"));
        basicShader.bind();
        basicShader.setUniform("projection", projectionHud);
        basicShader.setUniform("modelview", new Matrix4().initIdentity());

        currentShader = basicShader;
        postRenderShader = new Shader(new String(loader.getResource(new ResourceLocation("ourcraft/shaders", "blit.vsh")).getData(), "UTF-8"), new String(loader.getResource(new ResourceLocation("ourcraft/shaders", "blit.fsh")).getData(), "UTF-8"));
        postRenderShader.bind();
        postRenderShader.setUniform("projection", projectionHud);
        postRenderShader.setUniform("modelview", new Matrix4().initIdentity());
    }

    /**
     * Starts World rendering
     */
    public void begin()
    {
        currentShader.bind();
        glBindFramebuffer(GL_FRAMEBUFFER, framebufferId);
        glClearColor(0, 1, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Ends World rendering
     */
    public void end()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        postRenderShader.bind();
        bindTexture(colorBuffer, 0);
        renderBuffer(renderBuffer);
        currentShader.bind();
    }

    /**
     * Pushs current RenderState
     */
    public RenderEngine pushState()
    {
        renderStatesStack.push(renderState);
        renderState = renderState.clone();
        return this;
    }

    /**
     * Pops current RenderState
     */
    public RenderEngine popState()
    {
        RenderState pop = renderStatesStack.pop();
        pop.apply(this);
        renderState = pop;
        return this;
    }

    public RenderState getRenderState()
    {
        return renderState;
    }

    public boolean isGLCapEnabled(int cap)
    {
        return glIsEnabled(cap);
    }

    /**
     * Calls glEnable(cap) and memorize the state of the cap
     */
    public RenderEngine enableGLCap(int cap)
    {
        glEnable(cap);
        renderState.setGLCap(cap, true);
        return this;
    }

    /**
     * Calls glDisable(cap) and memorize the state of the cap
     */
    public RenderEngine disableGLCap(int cap)
    {
        glDisable(cap);
        renderState.setGLCap(cap, false);
        return this;
    }

    /**
     * Calls glClearColor and memorize given parameters
     */
    public RenderEngine setClearColor(float r, float g, float b, float a)
    {
        glClearColor(r, g, b, a);
        renderState.setClearColor(r, g, b, a);
        return this;
    }

    /**
     * Calls glAlphaFunc and memorize given parameters
     */
    public RenderEngine setAlphaFunc(int func, float ref)
    {
        glAlphaFunc(func, ref);
        renderState.setAlphaFunc(func, ref);
        return this;
    }

    public Matrix4 getHUDProjectionMatrix()
    {
        return projectionHud;
    }

    public Texture getColorBuffer()
    {
        return colorBuffer;
    }

    public Frustum getFrustum()
    {
        return frustum;
    }

    public ITextureObject getByLocation(ResourceLocation loc)
    {
        return texturesLocs.get(loc);
    }
}
