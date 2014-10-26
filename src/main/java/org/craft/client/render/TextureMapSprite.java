package org.craft.client.render;

import java.awt.image.*;

import org.craft.resources.*;

public class TextureMapSprite implements ITickable
{
    protected ResourceLocation location;
    protected TextureIcon      icon;
    protected BufferedImage    rawImage;

    protected boolean          useRawImage = false;

    public void tick()
    {
        ;
    }
}
