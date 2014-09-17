package org.craft.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipSimpleResourceLoader extends ResourceLoader
{

	private HashMap<String, AbstractResource> resources = new HashMap<String, AbstractResource>();
	private AbstractResource				  baseRes;

	public ZipSimpleResourceLoader(AbstractResource res)
	{
		this.baseRes = res;
	}

	@Override
	public AbstractResource getResource(ResourceLocation location) throws Exception
	{
		if(!isLoaded(location))
		{
			InputStream stream = getInputStream(location);
			if(stream != null)
				resources.put(location.getFullPath(), new SimpleResource(location, stream, this));
			else
				throw new FileNotFoundException("Resource /" + location.getFullPath() + " not found.");
		}
		if(!isLoaded(location)) throw new FileNotFoundException("Resource /" + location.getFullPath() + " not found.");
		return resources.get(location.getFullPath());
	}

	private InputStream getInputStream(ResourceLocation location)
	{
		try
		{
			ZipInputStream inputStream = new ZipInputStream(new ByteArrayInputStream(baseRes.getData()));
			ZipEntry e;
			ByteArrayOutputStream dataWriter = new ByteArrayOutputStream();
			while((e = inputStream.getNextEntry()) != null)
			{
				boolean save = false;
				if(e.getName().equals(location.getFullPath())) save = true;
				byte[] buffer = new byte[65565];
				int i;
				while((i = inputStream.read(buffer, 0, buffer.length)) != -1)
					if(save) dataWriter.write(buffer, 0, i);

				if(save) break;
			}
			dataWriter.flush();
			dataWriter.close();

			inputStream.close();

			return new ByteArrayInputStream(dataWriter.toByteArray());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
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
			if(ClasspathSimpleResourceLoader.class.getResource("/" + location.getFullPath()) != null) return true;
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}

}
