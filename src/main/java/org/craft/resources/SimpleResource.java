package org.craft.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class SimpleResource extends AbstractResource
{

    private byte[] bytes;

    public SimpleResource(ResourceLocation location, InputStream inputStream, ResourceLoader loader)
    {
        super(location, inputStream, loader);
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

    public File asFile()
    {
        throw new IllegalArgumentException("Impossible to retrieve simple resource as file");
    }
}
