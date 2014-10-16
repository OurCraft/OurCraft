package org.craft.client.render;

public class NullTextureIcon implements TextureIcon
{

    @Override
    public float getWidth()
    {
        return 1;
    }

    @Override
    public float getHeight()
    {
        return 1;
    }

    @Override
    public float getMinU()
    {
        return 0;
    }

    @Override
    public float getMaxU()
    {
        return 1;
    }

    @Override
    public float getMinV()
    {
        return 0;
    }

    @Override
    public float getMaxV()
    {
        return 1;
    }

}
