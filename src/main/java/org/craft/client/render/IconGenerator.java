package org.craft.client.render;

import org.craft.resources.*;

public interface IconGenerator
{
    /**
     * Generates an icon from the given ResourceLocation
     */
    public TextureIcon generateIcon(ResourceLocation loc);

    /**
     * Generates an icon from the given String
     */
    public TextureIcon generateIcon(String loc);
}
