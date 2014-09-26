package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.*;

import org.craft.client.*;
import org.craft.entity.*;
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
    private Shader                                    basicShader;
    private Matrix4                                   projectionHud;
    private Matrix4                                   modelMatrix;
    private Matrix4                                   projection;
    private boolean                                   projectFromEntity;
    private int                                       blendSrc;
    private int                                       blendDst;
    private ResourceLoader                            loader;

    public RenderEngine(ResourceLoader loader) throws Exception
    {
        this.loader = loader;
        texturesLocs = new HashMap<ResourceLocation, ITextureObject>();
        projectFromEntity = true;
        modelMatrix = new Matrix4().initIdentity();
        projection3dMatrix = new Matrix4().initPerspective((float) Math.toRadians(90), 16f / 9f, 0.001f, 1000);
        projection = projection3dMatrix;
        projectionHud = new Matrix4().initOrthographic(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        basicShader = new Shader(new String(loader.getResource(new ResourceLocation("ourcraft/shaders", "base.vsh")).getData(), "UTF-8"), new String(loader.getResource(new ResourceLocation("ourcraft/shaders", "base.fsh")).getData(), "UTF-8"));
        basicShader.bind();
        basicShader.setUniform("projection", projectionHud);
        basicShader.setUniform("modelview", new Matrix4().initIdentity());

        currentShader = basicShader;
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
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
            return projection.mul(renderViewEntity.getRotation().conjugate().toRotationMatrix().mul(new Matrix4().initTranslation(-renderViewEntity.getX() - 0.5f, -renderViewEntity.getY() - renderViewEntity.getEyeOffset(), -renderViewEntity.getZ() - 0.5f)));
        }
        return projection;
    }

    public void setRenderViewEntity(Entity e)
    {
        this.renderViewEntity = e;
    }

    /**
     * Calls glEnable(cap)<br/>
     * Will be able to memorize render state in the future
     */
    public void enableGLCap(int cap)
    {
        glEnable(cap);
    }

    /**
     * Calls glDisable(cap)<br/>
     * Will be able to memorize render state in the future
     */
    public void disableGLCap(int cap)
    {
        glDisable(cap);
    }

    public Entity getRenderViewEntity()
    {
        return renderViewEntity;
    }

    public void renderSplashScreen()
    {
        OpenGLBuffer buffer = new OpenGLBuffer();
        buffer.addVertex(new Vertex(Vector3.get(0, 0, 0), Vector2.get(0, 0)));
        buffer.addVertex(new Vertex(Vector3.get(OurCraft.getOurCraft().getDisplayWidth(), 0, 0), Vector2.get(1, 0)));
        buffer.addVertex(new Vertex(Vector3.get(OurCraft.getOurCraft().getDisplayWidth(), OurCraft.getOurCraft().getDisplayHeight(), 0), Vector2.get(1, 1)));
        buffer.addVertex(new Vertex(Vector3.get(0, OurCraft.getOurCraft().getDisplayHeight(), 0), Vector2.get(0, 1)));

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
        this.blendSrc = blendSrc;
        this.blendDst = blendDst;
        glBlendFunc(blendSrc, blendDst);
    }

    public void bindTexture(ITextureObject object)
    {
        bindTexture(object, 0);
    }

    public void bindTexture(ITextureObject object, int slot)
    {
        if(object != null)
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
            object.bind();
        }
    }

    public void bindTexture(int texId, int slot)
    {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, texId);
    }

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
                Log.debug("Unknown texture: " + loc.getFullPath() + " charging it");
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

    public void registerLocation(ResourceLocation loc, ITextureObject object)
    {
        texturesLocs.put(loc, object);
    }

    public void dispose()
    {
        for(ITextureObject o : texturesLocs.values())
            if(o instanceof IDisposable)
                ((IDisposable) o).dispose();
        currentShader.dispose();
    }

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
    }
}
