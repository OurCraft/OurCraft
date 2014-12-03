package org.craft.client.models;

import java.util.*;

import com.google.common.collect.Lists;

/**
 * UNDOCUMENTED: System far from being done and/or even kept
 */
public class ModelBase
{

    private List<ModelBox> boxes;

    public ModelBase()
    {
        boxes = Lists.newArrayList();
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

    public void addBox(ModelBox box)
    {
        boxes.add(box);
    }

}
