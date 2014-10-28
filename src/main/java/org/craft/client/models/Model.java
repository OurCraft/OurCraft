package org.craft.client.models;

import java.util.*;

public class Model
{

    private HashMap<String, String> textures;
    private ArrayList<ModelElement> elements;
    private String                  name;

    public Model(String name)
    {
        this.name = name;
        textures = new HashMap<String, String>();
        elements = new ArrayList<ModelElement>();
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
    public void copyFrom(Model other)
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
    public void addElement(ModelElement loadedElement)
    {
        elements.add(loadedElement);
    }

    /**
     * Gets element from given index, or null if inexistant
     */
    public ModelElement getElement(int index)
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
