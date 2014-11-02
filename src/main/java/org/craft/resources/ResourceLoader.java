package org.craft.resources;

import java.io.*;
import java.util.*;

public abstract class ResourceLoader
{

    public ResourceLoader()
    {

    }

    /**
     * Loads a Resource from given location
     */
    public abstract AbstractResource getResource(ResourceLocation location) throws IOException;

    /**
     * Returns List of Resources from given location
     */
    public abstract List<AbstractResource> getAllResources(ResourceLocation location) throws IOException;

    /**
     * Checks if Resource exists
     */
    public abstract boolean doesResourceExists(ResourceLocation location);

    public AbstractResource getResourceOrCreate(ResourceLocation resourceLocation) throws IOException
    {
        return getResource(resourceLocation);
    }

}
