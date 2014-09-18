package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import org.craft.entity.*;
import org.craft.maths.*;
import org.lwjgl.opengl.*;

public class RenderEngine
{

    private Entity  renderViewEntity;
    private Matrix4 projection;

    public RenderEngine()
    {
        glEnable(GL_DEPTH_TEST);
        projection = new Matrix4().initPerspective((float)Math.toRadians(90), 16f / 9f, 0.001f, 1000);
    }

    public void renderBuffer(OpenGLBuffer buffer, ITextureObject texture)
    {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + 0);
        texture.bind();
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, buffer.getVboID());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE_IN_FLOATS * 4, 12);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer.getIboID());
        glDrawElements(GL_TRIANGLES, buffer.getIndicesCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
    }

    public Matrix4 getProjectionMatrix()
    {
        if(renderViewEntity != null)
        {
            return projection.mul(renderViewEntity.getRotation().conjugate().toRotationMatrix().mul(new Matrix4().initTranslation(-renderViewEntity.getPos().getX() - 0.5f, -renderViewEntity.getPos().getY() - renderViewEntity.getEyeOffset(), -renderViewEntity.getPos().getZ() - 0.5f)));
        }
        return projection;
    }

    public void setRenderViewEntity(Entity e)
    {
        this.renderViewEntity = e;
    }

    public void enableGLCap(int cap)
    {
        glEnable(cap);
    }

    public void disableGLCap(int cap)
    {
        glDisable(cap);
    }
}
