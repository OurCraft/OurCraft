package org.craft.client.render;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import org.craft.maths.*;
import org.craft.utils.*;

public class Stitcher
{
    private ArrayList<BufferedImage> imgs;
    private ArrayList<Slot>          slots;
    private int                      tileWidth;
    private int                      tileHeight;
    private BufferedImage            emptySlotImage;
    private boolean                  putInCorner;

    public Stitcher(BufferedImage emptyImage, boolean putInCorner)
    {
        this.emptySlotImage = emptyImage;
        slots = new ArrayList<Slot>();
        imgs = new ArrayList<BufferedImage>();
        this.putInCorner = putInCorner;
        tileWidth = -1;
        tileHeight = -1;
    }

    /**
     * Adds a image to the list
     */
    public int addImage(BufferedImage img)
    {
        return addImage(img, false);
    }

    /**
     * Adds a image to the list and resizes it if asked
     */
    public int addImage(BufferedImage img, boolean forceResize)
    {
        if(tileWidth == -1 || tileHeight == -1)
        {
            tileWidth = img.getWidth();
            tileHeight = img.getHeight();
        }
        else if((img.getWidth() != tileWidth || img.getHeight() != tileHeight))
        {
            if(!forceResize && !putInCorner)
            {
                Log.fatal("Unexpected size: " + img.getWidth() + "x" + img.getHeight() + "px, expected " + tileWidth + "x" + tileHeight + "px. Image index: " + imgs.size());
            }
            else if(forceResize)
            {
                img = ImageUtils.resize(img, tileWidth, tileHeight);
            }
        }
        imgs.add(img);
        return imgs.size() - 1;
    }

    /**
     * Creates a big BufferImage containing all previously given images
     */
    public BufferedImage stitch()
    {
        int nbrY = MathHelper.upperPowerOf2((int) Math.floor(Math.sqrt(imgs.size())));
        int nbrX = (int) Math.ceil((double) imgs.size() / (double) nbrY);

        while((nbrX * nbrY - (imgs.size() - 1)) > nbrY)
            nbrY-- ;
        int width = nbrX * tileWidth;
        int height = nbrY * tileHeight;
        if(height < tileHeight)
            height = tileHeight;
        if(width < tileWidth)
            width = tileWidth;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.createGraphics();
        for(int i = 0; i < imgs.size(); i++ )
        {
            int column = i % nbrX;
            int row = i / nbrX;
            int x = column * tileWidth;
            int y = row * tileHeight;
            g.drawImage(imgs.get(i), column * tileWidth, row * tileHeight, null);
            slots.add(new Slot((float) x / (float) width, (float) y / (float) height, (float) (x + tileWidth) / (float) width, (float) (y + tileHeight) / (float) height, width, height));
        }

        emptySlotImage = ImageUtils.resize(emptySlotImage, tileWidth, tileHeight);
        for(int n = imgs.size(); n < nbrX * nbrY; n++ )
        {
            int column = n % nbrX;
            int row = n / nbrX;
            g.drawImage(emptySlotImage, column * tileWidth, row * tileHeight, null);
        }
        g.dispose();
        return result;
    }

    /**
     * Gets min U coordinate for given index 
     */
    public float getMinU(int index)
    {
        return slots.get(index).minU;
    }

    /**
     * Gets min V coordinate for given index 
     */
    public float getMinV(int index)
    {
        return slots.get(index).minV;
    }

    /**
     * Gets max U coordinate for given index 
     */
    public float getMaxU(int index)
    {
        return slots.get(index).maxU;
    }

    /**
     * Gets max V coordinate for given index 
     */
    public float getMaxV(int index)
    {
        return slots.get(index).maxV;
    }

    /**
     * Gets width for given index 
     */
    public int getWidth(int index)
    {
        return slots.get(index).width;
    }

    /**
     * Gets height for given index
     */
    public int getHeight(int index)
    {
        return slots.get(index).height;
    }

    private class Slot
    {
        public float minU;
        public float minV;
        public float maxU;
        public float maxV;
        public int   width;
        public int   height;

        Slot(float minU, float minV, float maxU, float maxV, int width, int height)
        {
            this.minU = minU;
            this.minV = minV;
            this.maxU = maxU;
            this.maxV = maxV;
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Sets tile width 
     */
    public void setTileWidth(int w)
    {
        tileWidth = w;
    }

    /**
     * Sets tile height
     */
    public void setTileHeight(int h)
    {
        tileHeight = h;
    }

    /**
     * Gets tile width 
     */
    public int getTileWidth()
    {
        return tileWidth;
    }

    /**
     * Gets tile height 
     */
    public int getTileHeight()
    {
        return tileHeight;
    }
}
