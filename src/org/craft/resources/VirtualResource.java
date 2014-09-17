package org.craft.resources;

import java.io.ByteArrayInputStream;

public class VirtualResource extends AbstractResource
{

	private byte[] bytes;

	public VirtualResource(String type, byte[] data, ResourceLoader loader)
	{
		super(new ResourceLocation(type), new ByteArrayInputStream(data), loader);
		this.bytes = data;
	}

	@Override
	public byte[] getData()
	{
		return bytes;
	}

}
