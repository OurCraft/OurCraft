package org.craft.resources;

import java.util.*;

public abstract class ResourceLoader
{

    public ResourceLoader()
    {

    }

    /**
     * Loads a Resource from given location
     */
    public abstract AbstractResource getResource(ResourceLocation location) throws Exception;

    /**
     * Returns List of Resources from given location
     */
    public abstract List<AbstractResource> getAllResources(ResourceLocation location) throws Exception;

    /**
     * Checks if Resource exists
     */
    public abstract boolean doesResourceExists(ResourceLocation location);

    public AbstractResource getResourceOrCreate(ResourceLocation resourceLocation) throws Exception
    {
        return getResource(resourceLocation);
    }

}
