package org.craft.resources;

import java.util.List;

public abstract class ResourceLoader
{

	public ResourceLoader()
	{

	}

	public abstract AbstractResource getResource(ResourceLocation location) throws Exception;

	public abstract List<AbstractResource> getAllResources(ResourceLocation location) throws Exception;

	public abstract boolean doesResourceExists(ResourceLocation location);

	public AbstractResource getResourceOrCreate(ResourceLocation resourceLocation) throws Exception
	{
		return getResource(resourceLocation);
	}

}
