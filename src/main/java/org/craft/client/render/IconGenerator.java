package org.craft.client.render;

import org.craft.resources.*;

public interface IconGenerator
{
    public TextureIcon generateIcon(ResourceLocation loc);

    public TextureIcon generateIcon(String loc);
}
