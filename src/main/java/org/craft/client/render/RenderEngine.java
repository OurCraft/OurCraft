package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import org.craft.client.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.lwjgl.opengl.*;

public class RenderEngine
{

    private Entity  renderViewEntity;
    private Matrix4 projection;

    public RenderEngine()
    {
        glEnable(GL_DEPTH_TEST);
        projection = new Matrix4().initPerspective((float) Math.toRadians(90), 16f / 9f, 0.001f, 1000);
    }

    /**
     * Renders a buffer with given texture
     */
    public void renderBuffer(OpenGLBuffer buffer, ITextureObject texture)
    {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + 0);
        texture.bind();
        renderBuffer(buffer);
    }

    /**
     * Renders a buffer
     */
    public void renderBuffer(OpenGLBuffer buffer)
    {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, buffer.getVboID());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 12);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 20);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer.getIboID());
        glDrawElements(GL_TRIANGLES, buffer.getIndicesCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glDisableClientState(GL_VERTEX_ARRAY);
    }

    public Matrix4 getProjectionMatrix()
    {
        if(renderViewEntity != null)
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
}
