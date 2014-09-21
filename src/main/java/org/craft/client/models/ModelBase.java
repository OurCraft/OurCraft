package org.craft.client.models;

import java.util.*;

public class ModelBase
{

    private ArrayList<ModelBox> boxes = new ArrayList<ModelBox>();

    public ModelBase()
    {

    }

    public ModelBox addBox(float x, float y, float z, float width, float height, float depth)
    {
        ModelBox box = new ModelBox(x, y, z, width, height, depth);
        boxes.add(box);
        return box;
    }

    public List<ModelBox> getChildren()
    {
        return boxes;
    }

}
