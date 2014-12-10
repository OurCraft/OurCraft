package org.craft.resources;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClasspathSimpleResourceLoader extends ResourceLoader
{

    private HashMap<String, AbstractResource> resources = new HashMap<String, AbstractResource>();
    private String                            base;

    public ClasspathSimpleResourceLoader()
    {
        this("");
    }

    public ClasspathSimpleResourceLoader(String base)
    {
        if(!base.endsWith("/"))
            base += "/";
        this.base = base;
    }

    @Override
    public AbstractResource getResource(ResourceLocation location) throws IOException
    {
        if(!isLoaded(location))
        {
            InputStream stream = ClasspathSimpleResourceLoader.class.getResourceAsStream("/" + base + location.getFullPath());
            if(stream != null)
            {
                URL url = ClasspathSimpleResourceLoader.class.getResource("/" + base + location.getFullPath());
                resources.put(location.getFullPath(), new SimpleResource(location, url, stream, this));
            }
            else
                throw new FileNotFoundException("Resource /" + base + location.getFullPath() + " not found.");
        }
        if(!isLoaded(location))
            throw new FileNotFoundException("Resource /" + base + location.getFullPath() + " not found.");
        return resources.get(location.getFullPath());
    }

    public boolean isLoaded(ResourceLocation location)
    {
        return resources.containsKey(location.getFullPath());
    }

    @Override
    public List<AbstractResource> getAllResources(ResourceLocation location) throws IOException
    {
        return Arrays.asList(getResource(location));
    }

    @Override
    public boolean doesResourceExists(ResourceLocation location)
    {
        try
        {
            if(ClasspathSimpleResourceLoader.class.getResource("/" + base + location.getFullPath()) != null)
                return true;
        }
        catch(Exception e)
        {
            return false;
        }
        return false;
    }

}
