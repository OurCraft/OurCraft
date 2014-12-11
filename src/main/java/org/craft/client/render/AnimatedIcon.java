package org.craft.client.render;

public class AnimatedIcon implements TextureIcon
{

    private TextureIcon[] tiles;
    private int           index;

    public AnimatedIcon(TextureIcon[] tiles)
    {
        this.tiles = tiles;
        this.index = 0;
    }

    public void incrementIndex()
    {
        index = index++ % tiles.length;
    }

    @Override
    public float getWidth()
    {
        return tiles[index].getWidth();
    }

    @Override
    public float getHeight()
    {
        return tiles[index].getHeight();
    }

    @Override
    public float getMinU()
    {
        return tiles[index].getMinU();
    }

    @Override
    public float getMaxU()
    {
        return tiles[index].getMaxU();
    }

    @Override
    public float getMinV()
    {
        return tiles[index].getMinV();
    }

    @Override
    public float getMaxV()
    {
        return tiles[index].getMaxV();
    }

}
