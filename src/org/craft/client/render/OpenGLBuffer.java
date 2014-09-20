package org.craft.client.render;

import static org.lwjgl.opengl.GL15.*;

import java.nio.*;
import java.util.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

public class OpenGLBuffer
{

    private int                 vboID;
    private int                 iboID;
    private LinkedList<Vertex>  vertices = new LinkedList<>();
    private LinkedList<Integer> indices  = new LinkedList<>();

    public OpenGLBuffer()
    {
        vboID = glGenBuffers();
        iboID = glGenBuffers();
    }

    public OpenGLBuffer(List<Vertex> vertices, List<Integer> indices)
    {
        this();
        this.setVertices(vertices);
        this.setIndices(indices);
    }

    public void upload()
    {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.size() * Vertex.SIZE_IN_FLOATS);
        for(Vertex vertex : vertices)
        {
            verticesBuffer.put(vertex.getPos().getX());
            verticesBuffer.put(vertex.getPos().getY());
            verticesBuffer.put(vertex.getPos().getZ());

            verticesBuffer.put(vertex.getTexCoords().getX());
            verticesBuffer.put(vertex.getTexCoords().getY());

            verticesBuffer.put(vertex.getColor().getX());
            verticesBuffer.put(vertex.getColor().getY());
            verticesBuffer.put(vertex.getColor().getZ());
        }
        verticesBuffer.flip();
        GL15.glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.size());
        for(int indice : indices)
        {
            indicesBuffer.put(indice);
        }
        indicesBuffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void setIndices(List<Integer> newIndices)
    {
        indices.clear();
        for(Integer index : newIndices)
        {
            indices.add(index);
        }
    }

    public void setVertices(List<Vertex> newVertices)
    {
        vertices.clear();
        for(Vertex v : newVertices)
        {
            vertices.add(v);
        }
    }

    public void addVertex(Vertex v)
    {
        vertices.add(v);
    }

    public void addIndex(int i)
    {
        indices.add(i);
    }

    public int getVboID()
    {
        return vboID;
    }

    public int getIboID()
    {
        return iboID;
    }

    public int getIndicesCount()
    {
        return indices.size();
    }

    public void clear()
    {
        indices.clear();
        vertices.clear();
        upload();
    }
}
