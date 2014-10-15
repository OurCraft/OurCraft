package org.craft.client.models;

import java.util.*;

import org.craft.maths.*;

public class BlockElement
{

    private HashMap<String, BlockFace> faces;
    private Vector3                    from;
    private Vector3                    to;

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
}
