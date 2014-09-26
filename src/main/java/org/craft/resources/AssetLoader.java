package org.craft.resources;

import java.util.*;

public class AssetLoader extends ResourceLoader
{

    private ClasspathSimpleResourceLoader mainLoader;
    private ResourceLoader                resourcePackLoader;

    public AssetLoader(ClasspathSimpleResourceLoader mainLoader)
    {
        this(mainLoader, null);
    }

    public AssetLoader(ClasspathSimpleResourceLoader mainLoader, ResourceLoader resourcePackLoader)
    {
        this.mainLoader = mainLoader;
        this.resourcePackLoader = resourcePackLoader;
    }

    @Override
    public AbstractResource getResource(ResourceLocation location) throws Exception
    {
        if(resourcePackLoader != null)
            if(resourcePackLoader.doesResourceExists(location))
            {
                return resourcePackLoader.getResource(location);
            }
        return mainLoader.getResource(location);
    }

    @Override
    public List<AbstractResource> getAllResources(ResourceLocation location) throws Exception
    {
        if(resourcePackLoader != null)
            if(resourcePackLoader.doesResourceExists(location))
            {
                return resourcePackLoader.getAllResources(location);
            }
        return mainLoader.getAllResources(location);
    }

    @Override
    public boolean doesResourceExists(ResourceLocation location)
    {
        if(resourcePackLoader != null)
            return mainLoader.doesResourceExists(location) || resourcePackLoader.doesResourceExists(location);
        return mainLoader.doesResourceExists(location);
    }

    public void setResourcePackLoader(ResourceLoader loader)
    {
        this.resourcePackLoader = loader;
    }

    public ResourceLoader getResourcePackLoader()
    {
        return resourcePackLoader;
    }

}
