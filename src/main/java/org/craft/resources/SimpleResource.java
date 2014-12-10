package org.craft.resources;

import java.io.*;
import java.net.*;

public class SimpleResource extends AbstractResource
{

    private byte[] bytes;
    private URL    url;

    public SimpleResource(ResourceLocation location, URL url, InputStream inputStream, ResourceLoader loader)
    {
        super(location, inputStream, loader);
        this.url = url;
    }

    @Override
    public byte[] getData()
    {
        if(bytes == null)
        {
            try
            {
                byte[] buffer = new byte[65565];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int i;
                while((i = getInputStream().read(buffer, 0, buffer.length)) != -1)
                {
                    baos.write(buffer, 0, i);
                }
                baos.flush();
                baos.close();
                bytes = baos.toByteArray();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public File asFile()
    {
        throw new IllegalArgumentException("Impossible to retrieve simple resource as file");
    }

    public URL getURL()
    {
        return url;
    }
}
