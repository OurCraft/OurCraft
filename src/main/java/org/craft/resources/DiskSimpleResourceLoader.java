package org.craft.resources;

import java.io.*;
import java.util.*;

public class DiskSimpleResourceLoader extends ResourceLoader
{

    private HashMap<String, AbstractResource> resources = new HashMap<String, AbstractResource>();
    private String                            base;

    public DiskSimpleResourceLoader()
    {
        this("");
    }

    public DiskSimpleResourceLoader(String base)
    {
        this.base = base;
    }

    @Override
    public AbstractResource getResourceOrCreate(ResourceLocation location) throws IOException
    {
        location = complete(location);
        if(!doesResourceExists(location))
        {
            File f = new File(location.getFullPath());
            if(!f.getParentFile().exists())
                f.getParentFile().mkdirs();
            f.createNewFile();
        }

        return getResource(location);
    }

    private ResourceLocation complete(ResourceLocation location)
    {
        return new ResourceLocation(base, location.getFullPath());
    }

    @Override
    public AbstractResource getResource(ResourceLocation location) throws IOException
    {
        location = complete(location);
        if(!isLoaded(location))
        {
            File file = new File(location.getFullPath());
            if(file.exists())
                resources.put(location.getFullPath(), new DiskSimpleResource(location, file, this));
            else
                throw new FileNotFoundException("Resource " + location.getFullPath() + " not found.");
        }
        if(!isLoaded(location))
            throw new FileNotFoundException("Resource " + location.getFullPath() + " not found.");
        return resources.get(location.getFullPath());
    }

    public boolean isLoaded(ResourceLocation location)
    {
        return resources.containsKey(location.getFullPath());
    }

    @Override
    public List<AbstractResource> getAllResources(ResourceLocation location) throws IOException
    {
        location = complete(location);
        return Arrays.asList(getResource(location));
    }

    @Override
    public boolean doesResourceExists(ResourceLocation location)
    {
        location = complete(location);
        return new File(location.getFullPath()).exists();
    }

}
