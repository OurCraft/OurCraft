package org.craft.client.models;

import org.craft.maths.*;

/**
 * UNDOCUMENTED: System far from being done and/or even kept
 */
public class ModelBox
{

    private float      x;
    private float      y;
    private float      z;
    private float      width;
    private float      height;
    private float      depth;
    private float      rotX;
    private float      rotY;
    private float      rotZ;
    private Quaternion rotation;

    public ModelBox(float x, float y, float z, float width, float height, float depth)
    {
        this.rotation = new Quaternion();
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public void setRotation(Quaternion rot)
    {
        this.rotation = rot;
    }

    public Quaternion getRotation()
    {
        return rotation;
    }

    /**
     * Sets a rotation point relative to the coordinates
     */
    public void setRotationPoint(float rx, float ry, float rz)
    {
        this.rotX = rx;
        this.rotY = ry;
        this.rotZ = rz;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public float getRotationPointX()
    {
        return rotX;
    }

    public float getRotationPointY()
    {
        return rotY;
    }

    public float getRotationPointZ()
    {
        return rotZ;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getDepth()
    {
        return depth;
    }

}
