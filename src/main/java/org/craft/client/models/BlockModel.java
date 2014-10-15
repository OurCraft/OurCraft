package org.craft.client.models;

import java.util.*;

public class BlockModel
{

    private HashMap<String, String> textures;
    private ArrayList<BlockElement> elements;

    public BlockModel()
    {
        textures = new HashMap<String, String>();
        elements = new ArrayList<BlockElement>();
    }

    public void copyFrom(BlockModel other)
    {
        textures.putAll(other.textures);
        elements.addAll(other.elements);
    }

    public void setTexturePath(String key, String texturePath)
    {
        textures.put(key, texturePath);
    }

    public String getTexturePath(String key)
    {
        return textures.get(key);
    }

    public void addElement(BlockElement loadedElement)
    {
        elements.add(loadedElement);
    }

    public BlockElement getElement(int index)
    {
        return elements.get(index);
    }

    public int getElementsCount()
    {
        return elements.size();
    }

}
