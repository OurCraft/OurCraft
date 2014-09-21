package org.craft.resources;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ClasspathSimpleResourceLoader extends ResourceLoader
{

    private HashMap<String, AbstractResource> resources = new HashMap<String, AbstractResource>();

    @Override
    public AbstractResource getResource(ResourceLocation location) throws Exception
    {
        if(!isLoaded(location))
        {
            InputStream stream = ClasspathSimpleResourceLoader.class.getResourceAsStream("/" + location.getFullPath());
            if(stream != null)
                resources.put(location.getFullPath(), new SimpleResource(location, stream, this));
            else
                throw new FileNotFoundException("Resource /" + location.getFullPath() + " not found.");
        }
        if(!isLoaded(location))
            throw new FileNotFoundException("Resource /" + location.getFullPath() + " not found.");
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
        try
        {
            if(ClasspathSimpleResourceLoader.class.getResource("/" + location.getFullPath()) != null)
                return true;
        }
        catch(Exception e)
        {
            return false;
        }
        return false;
    }

}
