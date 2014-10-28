package org.craft.client.models;

import java.util.*;

import org.craft.maths.*;

public class ModelElement
{

    /**
     * Maps string names to BlockFace instances
     */
    private HashMap<String, ModelFace> faces;

    /**
     * Starting point to render box
     */
    private Vector3                    from;

    /**
     * Ending point to render box
     */
    private Vector3                    to;

    /**
     * Boolean deciding if this element has a rotation
     */
    private boolean                    hasRotation;

    /**
     * Origin of element's rotation
     */
    private Vector3                    rotationOrigin;

    /**
     * Angle of rotation
     */
    private float                      angle;

    /**
     * Axis on which the rotation is effected
     */
    private String                     rotAxis;

    /**
     * Should we rescale after a rotation ?
     */
    private boolean                    rescale;

    public ModelElement()
    {
        faces = new HashMap<String, ModelFace>();
    }

    /**
     * Sets 'from' vector
     */
    public void setFrom(Vector3 from)
    {
        this.from = from;
    }

    /**
     * Sets 'to' vector
     */
    public void setTo(Vector3 to)
    {
        this.to = to;
    }

    /**
     * Gets 'from' vector
     */
    public Vector3 getFrom()
    {
        return from;
    }

    /**
     * Gets 'to' vector
     */
    public Vector3 getTo()
    {
        return to;
    }

    /**
     * Registers given face with given name 
     */
    public void setFace(String name, ModelFace face)
    {
        faces.put(name, face);
    }

    /**
     * Returns face with given name
     */
    public ModelFace getFace(String name)
    {
        return faces.get(name);
    }

    /**
     * Returns internal map containing all faces
     */
    public HashMap<String, ModelFace> getFaces()
    {
        return faces;
    }

    /**
     * Sets the axis on which the rotation is done 
     */
    public void setRotationAxis(String axis)
    {
        this.rotAxis = axis;
    }

    /**
     * Gets the axis on which the rotation is done 
     */
    public String getRotationAxis()
    {
        return rotAxis;
    }

    /**
     * Sets the angle of the rotation 
     */
    public void setRotationAngle(float angle)
    {
        this.angle = angle;
    }

    /**
     * Gets the angle of the rotation 
     */
    public float getRotationAngle()
    {
        return angle;
    }

    /**
     * Gets the origin of the rotation 
     */
    public Vector3 getRotationOrigin()
    {
        return rotationOrigin;
    }

    /**
     * Sets the origin of the rotation 
     */
    public void setRotationOrigin(Vector3 origin)
    {
        this.rotationOrigin = origin;
    }

    /**
     * Returns if this element has a rotation
     */
    public boolean hasRotation()
    {
        return hasRotation;
    }

    /**
     * Sets flag deciding if this element has a rotation
     */
    public void setHasRotation(boolean hasRotation)
    {
        this.hasRotation = hasRotation;
    }

    /**
     * Sets flag deciding if this element must be rescaled after a rotation
     */
    public void shouldRescale(boolean rescale)
    {
        this.rescale = rescale;
    }

    /**
     * Gets flag deciding if this element must be rescaled after a rotation
     */
    public boolean shouldRescale()
    {
        return rescale;
    }
}
