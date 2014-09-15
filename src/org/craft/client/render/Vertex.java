package org.craft.client.render;

import org.lwjgl.util.vector.*;

public class Vertex
{

    /**
     * 3 floats for the position
     * 2 floats for the tex coords
     */
    public static final int SIZE_IN_FLOATS = 3 + 2;
    private Vector3f        pos;
    private Vector2f        texCoords;

    public Vertex(Vector3f pos, Vector2f texCoords)
    {
        this.pos = pos;
        this.texCoords = texCoords;
    }

    public Vector3f getPos()
    {
        return pos;
    }

    public Vector2f getTexCoords()
    {
        return texCoords;
    }

}
