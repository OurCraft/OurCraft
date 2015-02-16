package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.*;
import java.util.*;

import org.craft.blocks.*;
import org.craft.client.*;
import org.craft.client.render.blocks.*;
import org.craft.client.render.items.*;
import org.craft.client.render.texture.*;
import org.craft.entity.*;
import org.craft.items.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.lwjgl.opengl.*;

public class RenderEngine implements IDisposable
{

    private HashMap<ResourceLocation, ITextureObject> texturesLocs;
    private Entity                                    renderViewEntity;
    private Matrix4                                   projection3dMatrix;
    private Shader                                    currentShader;
    private Matrix4                                   projectionHud;
    private Matrix4                                   modelMatrix;
    private Matrix4                                   projection;
    private boolean                                   projectFromEntity;
    private ResourceLoader                            loader;
    private OpenGLBuffer                              renderBuffer;
    private Shader                                    blitShader;
    private RenderState                               renderState;
    private Stack<RenderState>                        renderStatesStack;
    private Matrix4                                   translationMatrix;
    private Frustum                                   frustum;
    private float                                     fov;
    private float                                     ratio;
    private float                                     nearDist;
    private float                                     farDist;
    private int                                       displayWidth;
    private int                                       displayHeight;
    public TextureMap                                 blocksAndItemsMap;
    public ResourceLocation                           blocksAndItemsMapLocation;
    private ShaderBatch                               shaderBatch;
    private ShaderBatch                               guiShaderBatch;
    private Framebuffer                               frameBuffer;
    private boolean                                   guiRendering;
    private ITextureObject                            lastBoundTexture;

    public RenderEngine(ResourceLoader loader) throws IOException
    {
        renderStatesStack = new Stack<RenderState>();
        renderState = new RenderState();
        this.loader = loader;
        texturesLocs = new HashMap<ResourceLocation, ITextureObject>();
        projectFromEntity = true;
        loadMatrices();
        loadShaders();
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

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
        ITextureObject last = lastBoundTexture;

        flushBuffer(buffer, mode);

    }

    public void flushBuffer(OpenGLBuffer buffer, int mode)
    {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, buffer.getVboID());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 12);
        glVertexAttribPointer(2, 4, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 20);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer.getIboID());
        OurCraft.printIfGLError("Before drawing buffer");
        glDrawElements(mode, buffer.getIndicesCount(), GL_UNSIGNED_INT, 0);
        OurCraft.printIfGLError("After drawing buffer");

        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Returns projected view (from entity if {@link #shouldProjectFromEntity()} returns true)
     */
    public Matrix4 getProjectedViewMatrix()
    {
        if(renderViewEntity != null && shouldProjectFromEntity())
        {
            if(translationMatrix == null)
                translationMatrix = Matrix4.get();
            Quaternion camRot = renderViewEntity.getQuaternionRotation();
            Vector3 camPos = Vector3.get(-renderViewEntity.posX - 0.5f, -renderViewEntity.posY - renderViewEntity.getEyeOffset(), -renderViewEntity.posZ - 0.5f);
            frustum.update(fov, ratio, nearDist, farDist, camPos, camRot.getForward(), camRot.getUp());
            camPos.dispose();
            return projection.mul(camRot.conjugate().toRotationMatrix().mul(translationMatrix.initTranslation(-renderViewEntity.posX - 0.5f, -renderViewEntity.posY - renderViewEntity.getEyeOffset(), -renderViewEntity.posZ - 0.5f)));
        }
        return projection;
    }

    /**
     * Sets the render view entity.
     * <br/>The render view entity is the entity at which the render engine will place the camera in order to render the scene
     */
    public void setRenderViewEntity(Entity e)
    {
        this.renderViewEntity = e;
    }

    /**
     * Gets the render view entity.
     * <br/>The render view entity is the entity at which the render engine will place the camera in order to render the scene
     */
    public Entity getRenderViewEntity()
    {
        return renderViewEntity;
    }

    /**
     * Renders splash screen.
     */
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

    /**
     * Sets the modelview matrix.<br/>Also update shaders to use the new model matrix.
     */
    public void setModelviewMatrix(Matrix4 modelMatrix)
    {
        this.modelMatrix = modelMatrix;
        updateOpenGL();
    }

    /**
     * Gets the modelview matrix.
     */
    public Matrix4 getModelviewMatrix()
    {
        return modelMatrix;
    }

    /**
     * Updates shaders (bind the current one and attach modelview & projected view matrices)
     */
    public void updateOpenGL()
    {
        currentShader.bind();
        currentShader.setUniform("modelview", this.modelMatrix);
        currentShader.setUniform("projection", getProjectedViewMatrix());
    }

    /**
     * Sets the current shader and binds it
     */
    public void setCurrentShader(Shader shader)
    {
        this.currentShader = shader;
        updateOpenGL();
    }

    /**
     * Gets the current shader
     */
    public Shader getCurrentShader()
    {
        return currentShader;
    }

    /**
     * Sets the projection matrix and updates shaders to take it in account
     */
    public void setProjectionMatrix(Matrix4 projection)
    {
        this.projection = projection;
        renderState.setProjection(projection);
        updateOpenGL();
    }

    /**
     * Sets the projection matrix
     */
    public Matrix4 getProjectionMatrix()
    {
        return projection;
    }

    public void switchToOrtho()
    {
        switchToOrtho(displayWidth, displayHeight);
    }

    /**
     * Sets the projection matrix to an orthogonal matrix
     * @param j 
     * @param i 
     */
    public void switchToOrtho(int w, int h)
    {
        projectionHud = new Matrix4().initOrthographic(0, w, h, 0, 0, 1f);
        glViewport(0, 0, w, h);
        projectFromEntity = false;
        setProjectionMatrix(projectionHud);
        OurCraft.printIfGLError("After switching to ortho, size is " + w + ", " + h);
    }

    /**
     * Sets the projection matrix to an perspective matrix
     */
    public void switchToPerspective()
    {
        projectFromEntity = true;
        setProjectionMatrix(projection3dMatrix);
    }

    /**
     * Returns true if the render engine should project the scene from the entity
     */
    public boolean shouldProjectFromEntity()
    {
        return projectFromEntity;
    }

    /**
     * Sets the flag if the render engine should project the scene from the entity
     */
    public void setProjectFromEntity(boolean flag)
    {
        this.projectFromEntity = flag;
    }

    /**
     * Sets the blending function of OpenGL
     */
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
            if(guiRendering) // hacks, hacks everywhere!
                lastBoundTexture = object;
        }
        OurCraft.printIfGLError("after texture bind");
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
            bindTexture(null, 0);
        }
        else if(!texturesLocs.containsKey(loc))
        {
            bindTexture(null, 0);
            try
            {
                texturesLocs.put(loc, OpenGLHelper.loadTexture(loader.getResource(loc)));
                bindLocation(loc);
                if(Dev.debug())
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
            bindTexture(texObject);
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
    @Override
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
    public void reloadLocations() throws IOException
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
        createBlockAndItemMap(OurCraft.getOurCraft().getRenderBlocks(), OurCraft.getOurCraft().getRenderItems());
    }

    public void createBlockAndItemMap(RenderBlocks renderBlocks, RenderItems renderItems)
    {
        blocksAndItemsMap = new TextureMap(OurCraft.getOurCraft().getAssetsLoader(), new ResourceLocation("ourcraft", "textures"), true, true);
        blocksAndItemsMapLocation = new ResourceLocation("ourcraft", "textures/atlases/texture0.png");
        for(Block b : Blocks.BLOCK_REGISTRY.values())
        {
            renderBlocks.getRenderer(b);
            renderItems.getRenderer(b);
        }
        for(Item b : Items.ITEM_REGISTRY.values())
        {
            renderItems.getRenderer(b);
        }
        try
        {
            blocksAndItemsMap.compile();
            registerLocation(blocksAndItemsMapLocation, blocksAndItemsMap);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads all required shaders
     */
    public void loadShaders() throws IOException
    {
        glUseProgram(0);
        if(blitShader != null)
            blitShader.dispose();
        blitShader = new Shader(new String(loader.getResource(new ResourceLocation("ourcraft/shaders", "blit.vsh")).getData(), "UTF-8"), new String(loader.getResource(new ResourceLocation("ourcraft/shaders", "blit.fsh")).getData(), "UTF-8"));
        blitShader.bind();
        blitShader.setUniform("projection", projectionHud);
        blitShader.setUniform("modelview", modelMatrix);
        setCurrentShader(blitShader);

        try
        {
            shaderBatch = new ShaderBatch(blitShader);
            guiShaderBatch = new ShaderBatch(blitShader);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public ShaderBatch getPostWorldRenderBatch()
    {
        return shaderBatch;
    }

    public ShaderBatch getPostGuiRenderBatch()
    {
        return guiShaderBatch;
    }

    /**
     * Starts World rendering
     */
    public void beginWorldRendering()
    {
        setCurrentShader(blitShader);
        OurCraft.printIfGLError("before rendering world");
        setCurrentShader(blitShader);
        frameBuffer.bind();
        glViewport(0, 0, displayWidth, displayHeight);
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Ends World rendering
     */
    public void flushWorldRendering()
    {
        OurCraft.printIfGLError("after rendering world");
        disableGLCap(GL_DEPTH_TEST);
        switchToOrtho();
        shaderBatch.apply(0, getColorBuffer(), renderBuffer, this);
        OurCraft.printIfGLError("after post-processing world");
        setCurrentShader(blitShader);
    }

    public void beginGuiRendering()
    {
        frameBuffer.bind();
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void flushGuiRendering()
    {
        guiShaderBatch.apply(0, getColorBuffer(), renderBuffer, this);
        setCurrentShader(blitShader);
    }

    /**
     * Pushs current RenderState
     */
    public RenderEngine pushState()
    {
        renderStatesStack.push(renderState);
        renderState = renderState.clone();
        OurCraft.printIfGLError("after pushing render engine state");
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
        OurCraft.printIfGLError("After popping render engine state");
        return this;
    }

    /**
     * Gets the current render state
     */
    public RenderState getRenderState()
    {
        return renderState;
    }

    /**
     * Returns true if given OpenGL cap is enabled
     */
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
        OurCraft.printIfGLError("after enabling GL cap: " + OpenGLHelper.getCapName(cap));
        renderState.setGLCap(cap, true);
        return this;
    }

    /**
     * Calls glDisable(cap) and memorize the state of the cap
     */
    public RenderEngine disableGLCap(int cap)
    {
        glDisable(cap);
        OurCraft.printIfGLError("after disabling GL cap: " + OpenGLHelper.getCapName(cap));
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

    /**
     * Gets the orthogonal matrix used to project onto the screen
     */
    public Matrix4 getHUDProjectionMatrix()
    {
        return projectionHud;
    }

    /**
     * Returns the color buffer
     */
    public Texture getColorBuffer()
    {
        return frameBuffer.getColorBuffer();
    }

    /**
     * Returns the frustum
     */
    public Frustum getFrustum()
    {
        return frustum;
    }

    /**
     * Returns a texture registered to given location or null if none is bound to this location
     */
    public ITextureObject getByLocation(ResourceLocation loc)
    {
        return texturesLocs.get(loc);
    }

    /**
     * Loads all matrices (projection/hud)
     */
    public void loadMatrices()
    {
        this.displayWidth = OurCraft.getOurCraft().getDisplayWidth();
        this.displayHeight = OurCraft.getOurCraft().getDisplayHeight();
        if(modelMatrix != null)
        {
            modelMatrix.dispose();
            if(projection != projection3dMatrix)
                projection.dispose();
            projection3dMatrix.dispose();
            projectionHud.dispose();
        }
        modelMatrix = Matrix4.get().initIdentity();
        fov = (float) Math.toRadians(90);
        ratio = (float) displayWidth / (float) displayHeight;
        nearDist = 0.1f;
        farDist = 10000f;
        projection3dMatrix = Matrix4.get().initPerspective(fov, ratio, nearDist, farDist);
        projection = projection3dMatrix;
        projectionHud = Matrix4.get().initOrthographic(0, displayWidth, displayHeight, 0, -1, 1);

        if(renderBuffer != null)
            renderBuffer.dispose();
        renderBuffer = new OpenGLBuffer();

        renderBuffer.addVertex(Vertex.get(Vector3.get(0, 0, 0), Vector2.get(0, 1)));
        renderBuffer.addVertex(Vertex.get(Vector3.get(displayWidth, 0, 0), Vector2.get(1, 1)));
        renderBuffer.addVertex(Vertex.get(Vector3.get(displayWidth, displayHeight, 0), Vector2.get(1, 0)));
        renderBuffer.addVertex(Vertex.get(Vector3.get(0, displayHeight, 0), Vector2.get(0, 0)));

        renderBuffer.addIndex(0);
        renderBuffer.addIndex(1);
        renderBuffer.addIndex(2);

        renderBuffer.addIndex(2);
        renderBuffer.addIndex(3);
        renderBuffer.addIndex(0);
        renderBuffer.upload();
        renderBuffer.clearAndDisposeVertices();

        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        frameBuffer = new Framebuffer(displayWidth, displayHeight);

    }
}
