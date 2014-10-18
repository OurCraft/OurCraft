package org.craft.client.models;

import java.util.*;

import org.craft.maths.*;

public class BlockElement
{

    private HashMap<String, BlockFace> faces;
    private Vector3                    from;
    private Vector3                    to;
    private boolean                    hasRotation;
    private Vector3                    rotationOrigin;
    private float                      angle;
    private String                     rotAxis;
    private boolean                    rescale;

    public BlockElement()
    {
        faces = new HashMap<String, BlockFace>();
    }

    public void setFrom(Vector3 from)
    {
        this.from = from;
    }

    public void setTo(Vector3 to)
    {
        this.to = to;
    }

    public Vector3 getFrom()
    {
        return from;
    }

    public Vector3 getTo()
    {
        return to;
    }

    public void setFace(String name, BlockFace face)
    {
        faces.put(name, face);
    }

    public BlockFace getFace(String name)
    {
        return faces.get(name);
    }

    public HashMap<String, BlockFace> getFaces()
    {
        return faces;
    }

    public void setRotationAxis(String axis)
    {
        this.rotAxis = axis;
    }

    public String getRotationAxis()
    {
        return rotAxis;
    }

    public void setRotationAngle(float angle)
    {
        this.angle = angle;
    }

    public float getRotationAngle()
    {
        return angle;
    }

    public Vector3 getRotationOrigin()
    {
        return rotationOrigin;
    }

    public void setRotationOrigin(Vector3 origin)
    {
        this.rotationOrigin = origin;
    }

    public boolean hasRotation()
    {
        return hasRotation;
    }

    public void setHasRotation(boolean hasRotation)
    {
        this.hasRotation = hasRotation;
    }

    public void shouldRescale(boolean rescale)
    {
        this.rescale = rescale;
    }

    public boolean shouldRescale()
    {
        return rescale;
    }
}
