package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import org.craft.client.*;
import org.lwjgl.opengl.*;

public class RenderEngine
{

    public RenderEngine()
    {
        glEnable(GL_DEPTH_TEST);
    }

    public void renderBuffer(OpenGLBuffer buffer, Texture texture)
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
}
