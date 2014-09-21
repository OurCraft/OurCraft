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

    public Stitcher(BufferedImage emptyImage)
    {
        this.emptySlotImage = emptyImage;
        slots = new ArrayList<>();
        imgs = new ArrayList<>();
        tileWidth = -1;
        tileHeight = -1;
    }

    public int addImage(BufferedImage img)
    {
        return addImage(img, false);
    }

    public int addImage(BufferedImage img, boolean forceResize)
    {
        if(tileWidth == -1 || tileHeight == -1)
        {
            tileWidth = img.getWidth();
            tileHeight = img.getHeight();
        }
        else if((img.getWidth() != tileWidth || img.getHeight() != tileHeight))
        {
            if(!forceResize)
            {
                Log.fatal("Unexpected size: " + img.getWidth() + "x" + img.getHeight() + "px, expected " + tileWidth + "x" + tileHeight + "px. Image index: " + imgs.size());
            }
            else
            {
                img = ImageUtils.resize(img, tileWidth, tileHeight);
            }
        }
        imgs.add(img);
        return imgs.size() - 1;
    }

    public BufferedImage stitch()
    {
        int nbrY = MathHelper.upperPowerOf2((int) Math.floor(Math.sqrt(imgs.size())));
        int nbrX = (int) Math.ceil((double) imgs.size() / (double) nbrY);

        while((nbrX * nbrY - imgs.size()) >= nbrY)
            nbrY-- ;
        int width = nbrX * tileWidth;
        int height = nbrY * tileHeight;
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

    public float getMinU(int index)
    {
        return slots.get(index).minU;
    }

    public float getMinV(int index)
    {
        return slots.get(index).minV;
    }

    public float getMaxU(int index)
    {
        return slots.get(index).maxU;
    }

    public float getMaxV(int index)
    {
        return slots.get(index).maxV;
    }

    public int getWidth(int index)
    {
        return slots.get(index).width;
    }

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

    public void setTileWidth(int w)
    {
        tileWidth = w;
    }

    public void setTileHeight(int h)
    {
        tileHeight = h;
    }
}
