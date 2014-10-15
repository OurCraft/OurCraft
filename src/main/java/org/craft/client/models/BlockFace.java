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

    public void setCullface(String cullface)
    {
        this.cullface = cullface;
    }

    public String getCullface()
    {
        return cullface;
    }

    public void setTexture(String texture)
    {
        this.texture = texture;
    }

    public String getTexture()
    {
        return texture;
    }

    public void setMinUV(Vector2 minUV)
    {
        this.minUV = minUV;
    }

    public void setMaxUV(Vector2 maxUV)
    {
        this.maxUV = maxUV;
    }

    public Vector2 getMinUV()
    {
        return minUV;
    }

    public Vector2 getMaxUV()
    {
        return maxUV;
    }
}
