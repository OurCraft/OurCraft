package org.craft.client.render.texture;

import org.craft.utils.*;

public class TextureAtlas implements ITextureObject, IDisposable
{

    private Texture           texture;
    private int               tileWidth;
    private int               tileHeight;
    private int               xSpacing;
    private int               ySpacing;

    /**
     * [Column][Row]
     */
    private TextureRegion[][] tiles;
    private int               xNbr;
    private int               yNbr;

    public TextureAtlas(Texture texture, int tileWidth, int tileHeight)
    {
        this(texture, tileWidth, tileHeight, 0, 0);
    }

    public TextureAtlas(Texture texture, int tileWidth, int tileHeight, int xSpacing, int ySpacing)
    {
        this.texture = texture;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.xSpacing = xSpacing;
        this.ySpacing = ySpacing;

        for(int i = 0; i < texture.getWidth(); i += tileWidth + xSpacing)
        {
            xNbr++ ;
        }

        for(int i = 0; i < texture.getHeight(); i += tileHeight + ySpacing)
        {
            yNbr++ ;
        }
        tiles = new TextureRegion[xNbr][yNbr];
        for(int x = 0; x < xNbr; x++ )
        {
            for(int y = 0; y < yNbr; y++ )
            {
                float minU = (x * (tileWidth + xSpacing)) / (float) texture.getWidth();
                float minV = (y * (tileHeight + ySpacing) + (tileHeight + ySpacing)) / (float) texture.getHeight();

                float maxU = (x * (tileWidth + xSpacing) + (tileWidth + xSpacing)) / (float) texture.getWidth();
                float maxV = (y * (tileHeight + ySpacing)) / (float) texture.getHeight();

                tiles[x][y] = new TextureRegion(texture, minU, minV, maxU, maxV);
            }
        }
    }

    /**
     * Gets the array generated by splitting the texture in regions. The returned array is organized by [Column][Row]
     */
    public TextureRegion[][] getTiles()
    {
        return tiles;
    }

    @Override
    public void dispose()
    {
        texture.dispose();
    }

    /**
     * Returns texture used to generate regions
     */
    public Texture getTexture()
    {
        return texture;
    }

    /**
     * Gets the tile width
     */
    public int getTileWidth()
    {
        return tileWidth;
    }

    /**
     * Gets the tile height
     */
    public int getTileHeight()
    {
        return tileHeight;
    }

    /**
     * Gets the spacing of tiles on X axis
     */
    public int getXSpacing()
    {
        return xSpacing;
    }

    /**
     * Gets the spacing of tiles on Y axis
     */
    public int getYSpacing()
    {
        return ySpacing;
    }

    /**
     * Gets the number of tiles on X axis
     */
    public int getXNbr()
    {
        return xNbr;
    }

    /**
     * Gets the number of tiles on Y axis
     */
    public int getYNbr()
    {
        return yNbr;
    }

    @Override
    public void bind()
    {
        texture.bind();
    }

}
