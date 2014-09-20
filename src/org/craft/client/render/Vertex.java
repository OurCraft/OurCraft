package org.craft.client.render;

import org.craft.maths.*;

public class Vertex
{

    /**
     * 3 floats for the position
     * 2 floats for the tex coords
     * 1 float for the red composite of color
     * 1 float for the green composite of color
     * 1 float for the blue composite of color
     */
    public static final int SIZE_IN_FLOATS = 3 + 2 + 3;
    private Vector3         pos;
    private Vector2         texCoords;
    private Vector3         color;

    public Vertex(Vector3 pos, Vector2 texCoords)
    {
        this(pos, texCoords, Vector3.get(1, 1, 1));
    }

    public Vertex(Vector3 pos, Vector2 texCoords, Vector3 color)
    {
        this.pos = pos;
        this.texCoords = texCoords;
        this.color = color;
    }

    public Vector3 getPos()
    {
        return pos;
    }

    public Vector2 getTexCoords()
    {
        return texCoords;
    }

    public Vector3 getColor()
    {
        return color;
    }

}
