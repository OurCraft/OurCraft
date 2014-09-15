package org.craft.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import org.craft.client.*;

public class RenderEngine
{

    public RenderEngine()
    {

    }

    public void renderBuffer(OpenGLBuffer buffer, Texture texture)
    {
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
