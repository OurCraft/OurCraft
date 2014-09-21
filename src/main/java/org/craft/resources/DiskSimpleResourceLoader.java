package org.craft.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DiskSimpleResourceLoader extends ResourceLoader
{

    private HashMap<String, AbstractResource> resources = new HashMap<String, AbstractResource>();

    @Override
    public AbstractResource getResourceOrCreate(ResourceLocation location) throws Exception
    {
        if(!doesResourceExists(location))
        {
            File f = new File(location.getFullPath());
            if(!f.getParentFile().exists())
                f.getParentFile().mkdirs();
            f.createNewFile();
        }

        return getResource(location);
    }

    @Override
    public AbstractResource getResource(ResourceLocation location) throws Exception
    {
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
    public List<AbstractResource> getAllResources(ResourceLocation location) throws Exception
    {
        return Arrays.asList(getResource(location));
    }

    @Override
    public boolean doesResourceExists(ResourceLocation location)
    {
        return new File(location.getFullPath()).exists();
    }

}
