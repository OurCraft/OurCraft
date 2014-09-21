package org.craft.client.render;

import org.craft.maths.*;

public class Vertex
{

    /**
     * Size of 1 Vertex in floats:<br/>
     * <ul>
     * <li>3 floats for the position</li>
     * <li>2 floats for the texture coordinates</li>
     * <li>1 float for the red composite of color</li>
     * <li>1 float for the green composite of color</li>
     * <li>1 float for the blue composite of color</li>
     * </ul>
     */
    public static final int SIZE_IN_FLOATS = 3 + 2 + 3;
    private Vector3         pos;
    private Vector2         texCoords;
    private Vector3         color;

    /**
     * Creates a Vertex with given position, texCoords and color set as white
     */
    public Vertex(Vector3 pos, Vector2 texCoords)
    {
        this(pos, texCoords, Vector3.get(1, 1, 1));
    }

    /**
     * Creates a Vertex with given position, texCoords and color
     */
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
