package org.craft.client.render.texture;

public class TextureRegion implements TextureIcon
{

    private Texture texture;
    private float   minU;
    private float   minV;
    private float   maxU;
    private float   maxV;

    public TextureRegion(Texture texture)
    {
        this(texture, 0, 0, 1, 1);
    }

    public TextureRegion(Texture texture, TextureIcon icon)
    {
        this(texture, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV());
    }

    public TextureRegion(Texture texture, float minU, float minV, float maxU, float maxV)
    {
        this.texture = texture;
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
    }

    /**
     * Gets texture from which this icon is extracted
     */
    public Texture getTexture()
    {
        return texture;
    }

    @Override
    public float getMinU()
    {
        return minU;
    }

    @Override
    public float getMinV()
    {
        return minV;
    }

    @Override
    public float getMaxU()
    {
        return maxU;
    }

    @Override
    public float getMaxV()
    {
        return maxV;
    }

    public TextureRegion setTexture(Texture text)
    {
        this.texture = text;
        return this;
    }

    public TextureRegion setMinV(float newMinV)
    {
        this.minV = newMinV;
        return this;
    }

    public TextureRegion setMaxU(float newMaxU)
    {
        this.maxU = newMaxU;
        return this;
    }

    public TextureRegion setMinU(float newMinU)
    {
        this.minU = newMinU;
        return this;
    }

    public TextureRegion setMaxV(float newMaxV)
    {
        this.maxV = newMaxV;
        return this;
    }

    public TextureRegion flip(boolean horizontaly, boolean verticaly)
    {
        if(horizontaly)
        {
            float oldMaxU = maxU;
            maxU = minU;
            minU = oldMaxU;
        }

        if(verticaly)
        {
            float oldMaxV = maxV;
            maxV = minV;
            minV = oldMaxV;
        }
        return this;
    }

    @Override
    public float getWidth()
    {
        return ((getMaxU() - getMinU()) * texture.getWidth());
    }

    @Override
    public float getHeight()
    {
        return ((getMaxV() - getMinV()) * texture.getHeight());
    }
}
