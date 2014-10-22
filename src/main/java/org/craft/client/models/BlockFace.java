package org.craft.client.models;

import org.craft.maths.*;

public class BlockFace
{

    private String  cullface;
    private String  texture;
    private Vector2 maxUV;
    private Vector2 minUV;

    public BlockFace()
    {
        minUV = Vector2.NULL;
        maxUV = Vector2.get(1, 1);
    }

    /**
     * Sets culling face.
     */
    public void setCullface(String cullface)
    {
        this.cullface = cullface;
    }

    /**
     * Returns culling face
     */
    public String getCullface()
    {
        return cullface;
    }

    /**
     * Sets texture used to render this face 
     */
    public void setTexture(String texture)
    {
        this.texture = texture;
    }

    /**
     * Gets texture used to render this face 
     */
    public String getTexture()
    {
        return texture;
    }

    /**
     * Sets min UV coordinates used to render this face
     */
    public void setMinUV(Vector2 minUV)
    {
        this.minUV = minUV;
    }

    /**
     * Sets max UV coordinates used to render this face
     */
    public void setMaxUV(Vector2 maxUV)
    {
        this.maxUV = maxUV;
    }

    /**
     * Gets min UV coordinates used to render this face
     */
    public Vector2 getMinUV()
    {
        return minUV;
    }

    /**
     * Gets max UV coordinates used to render this face
     */
    public Vector2 getMaxUV()
    {
        return maxUV;
    }
}
