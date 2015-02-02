package org.craft.client.render.texture;

import java.awt.image.*;

import org.craft.client.render.ITickable;
import org.craft.resources.*;

public class TextureMapSprite implements ITickable
{
    protected ResourceLocation location;
    protected TextureIcon      icon;
    protected BufferedImage    rawImage;

    protected boolean          useRawImage = false;

    @Override
    public void tick()
    {
        ;
    }
}
