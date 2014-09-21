package org.craft.resources;

import java.io.*;

public abstract class AbstractResource
{

    private ResourceLocation location;
    private InputStream      inputStream;
    private ResourceLoader   loader;

    public AbstractResource(ResourceLocation location, InputStream inputStream, ResourceLoader loader)
    {
        this.location = location;
        this.inputStream = inputStream;
        this.loader = loader;
    }

    public ResourceLoader getLoader()
    {
        return loader;
    }

    public InputStream getInputStream()
    {
        return inputStream;
    }

    public ResourceLocation getResourceLocation()
    {
        return location;
    }

    public boolean equals(Object o)
    {
        if(o instanceof AbstractResource)
        {
            AbstractResource res = (AbstractResource) o;

            return res.getResourceLocation().getFullPath().equals(getResourceLocation().getFullPath()) && res.getLoader().getClass() == getLoader().getClass();
        }
        return false;
    }

    public int hashCode()
    {
        final int BASE = 17;
        final int MULTIPLIER = 31;

        int result = BASE;
        result = MULTIPLIER * result + getResourceLocation().getSection().hashCode();
        result = MULTIPLIER * result + getResourceLocation().getPath().hashCode();

        return result;
    }

    public File asFile()
    {
        throw new IllegalArgumentException("Impossible to retrieve resource as file");
    }

    /**
     * Returns raw data from resource
     */
    public abstract byte[] getData();
}
