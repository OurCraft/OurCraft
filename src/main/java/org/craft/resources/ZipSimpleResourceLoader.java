package org.craft.resources;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class ZipSimpleResourceLoader extends ResourceLoader
{

    private HashMap<String, AbstractResource> resources = new HashMap<String, AbstractResource>();
    private AbstractResource                  baseRes;
    private String                            base;

    public ZipSimpleResourceLoader(AbstractResource res)
    {
        this(res, "");
    }

    public ZipSimpleResourceLoader(AbstractResource res, String base)
    {
        this.baseRes = res;
        this.base = base;
        init();
    }

    private void init()
    {
        try
        {
            ZipInputStream inputStream = new ZipInputStream(new ByteArrayInputStream(baseRes.getData()));
            ZipEntry e;
            ByteArrayOutputStream dataWriter;
            ResourceLocation currentLoc;
            while((e = inputStream.getNextEntry()) != null)
            {
                if(e.isDirectory())
                    continue;
                dataWriter = new ByteArrayOutputStream();
                currentLoc = new ResourceLocation(e.getName());
                byte[] buffer = new byte[65565];
                int i;
                while((i = inputStream.read(buffer, 0, buffer.length)) != -1)
                    dataWriter.write(buffer, 0, i);

                dataWriter.flush();
                dataWriter.close();
                resources.put(currentLoc.getFullPath(), new SimpleResource(currentLoc, new ByteArrayInputStream(dataWriter.toByteArray()), this));
            }

            inputStream.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractResource getResource(ResourceLocation location) throws Exception
    {
        ResourceLocation l = complete(location);
        if(!isLoaded(location))
            throw new FileNotFoundException("Resource /" + l.getFullPath() + " not found.");
        return resources.get(l.getFullPath());
    }

    private ResourceLocation complete(ResourceLocation location)
    {
        if(base.equals(""))
            return location;
        return new ResourceLocation(base + "/" + location.getFullPath());
    }

    public boolean isLoaded(ResourceLocation location)
    {
        location = complete(location);
        return resources.containsKey(location.getFullPath());
    }

    @Override
    public List<AbstractResource> getAllResources(ResourceLocation location) throws Exception
    {
        location = complete(location);
        return Arrays.asList(getResource(location));
    }

    @Override
    public boolean doesResourceExists(ResourceLocation location)
    {
        location = complete(location);
        return resources.containsKey(location.getFullPath());
    }

}
