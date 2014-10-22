package org.craft.client.render;

import java.util.*;

import org.craft.maths.*;
import org.craft.utils.*;

public class Vertex extends AbstractReference implements IDisposable
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
     * Creates a Vertex with given position and texCoords and color set as 0,0,0
     */
    private Vertex(Vector3 pos)
    {
        this(pos, Vector2.get(0, 0));
    }

    /**
     * Creates a Vertex with given position, texCoords and white color
     */
    private Vertex(Vector3 pos, Vector2 texCoords)
    {
        this(pos, texCoords, Vector3.get(1, 1, 1));
    }

    /**
     * Creates a Vertex with given position, texCoords and color
     */
    private Vertex(Vector3 pos, Vector2 texCoords, Vector3 color)
    {
        this.pos = pos;
        this.texCoords = texCoords;
        this.color = color;
    }

    /**
     * Returns the position vector of this vertex
     */
    public Vector3 getPos()
    {
        return pos;
    }

    /**
     * Returns the uv coords vector of this vertex
     */
    public Vector2 getTexCoords()
    {
        return texCoords;
    }

    /**
     * Returns the color vector of this vertex (RGB color model)
     */
    public Vector3 getColor()
    {
        return color;
    }

    @Override
    public void dispose()
    {
        pos.dispose();
        color.dispose();
        texCoords.dispose();

        if(decreaseReferenceCounter())
        {
            unused.push(this);
        }
    }

    private static Stack<Vertex> unused = new Stack<Vertex>();

    /**
     * Returns a vertex with given position
     */
    public static Vertex get(Vector3 pos)
    {
        return get(pos, Vector2.NULL);
    }

    /**
     * Returns a vertex with given position and texture coordinates
     */
    public static Vertex get(Vector3 pos, Vector2 texCoords)
    {
        return get(pos, texCoords, Vector3.get(1, 1, 1));
    }

    /**
     * Returns a vertex with given position, texture coordinates and color
     */
    public static Vertex get(Vector3 pos, Vector2 texCoords, Vector3 color)
    {
        Vertex v = null;
        if(unused.isEmpty())
        {
            v = new Vertex(pos, texCoords, color);
        }
        else
        {
            v = unused.pop();
            v.pos = pos;
            v.texCoords = texCoords;
            v.color = color;
        }
        v.increaseReferenceCounter();
        return v;
    }

    /**
     * Returns an uncached copy of this vertex.
     */
    public Vertex copy()
    {
        return new Vertex(pos.copy(), texCoords.copy(), color.copy());
    }

}
