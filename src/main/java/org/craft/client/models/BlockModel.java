package org.craft.client.models;

import java.util.*;

public class BlockModel
{

    private HashMap<String, String> textures;
    private ArrayList<BlockElement> elements;
    private String                  name;

    public BlockModel(String name)
    {
        this.name = name;
        textures = new HashMap<String, String>();
        elements = new ArrayList<BlockElement>();
    }

    /**
     * Returns model name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Copy all data from given model.
     */
    public void copyFrom(BlockModel other)
    {
        textures.putAll(other.textures);
        elements.addAll(other.elements);
    }

    /**
     * Sets texture path for given texture variable
     */
    public void setTexturePath(String key, String texturePath)
    {
        textures.put(key, texturePath);
    }

    /**
     * Gets texture path for given texture variable
     */
    public String getTexturePath(String key)
    {
        return textures.get(key);
    }

    /**
     * Adds a new element to this model
     */
    public void addElement(BlockElement loadedElement)
    {
        elements.add(loadedElement);
    }

    /**
     * Gets element from given index, or null if inexistant
     */
    public BlockElement getElement(int index)
    {
        return elements.get(index);
    }

    /**
     * Returns number of elements in this model
     */
    public int getElementsCount()
    {
        return elements.size();
    }

}
