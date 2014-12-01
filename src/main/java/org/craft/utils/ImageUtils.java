package org.craft.utils;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

import org.craft.resources.*;
import org.lwjgl.*;

public class ImageUtils
{

    private static HashMap<String, BufferedImage>           imgs      = new HashMap<String, BufferedImage>();
    private static HashMap<AbstractResource, BufferedImage> imagesMap = new HashMap<AbstractResource, BufferedImage>();

    public static BufferedImage toBufferedImage(Image i)
    {
        BufferedImage result = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(i, 0, 0, null);
        g.dispose();
        return result;
    }

    public static Color getColor(int color, boolean hasAlpha)
    {
        float a = 1f;
        if(hasAlpha)
        {
            a = ((color >> 24) & 0xFF) / 255f;
        }
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = ((color >> 0) & 0xFF) / 255f;
        return new Color(r, g, b, a);
    }

    public static Color getColor(int color)
    {
        return getColor(color, false);
    }

    public static BufferedImage recolor(BufferedImage src, Color sc)
    {
        float[][] colorMatrix =
        {
                {
                        ((float) sc.getRed()) / 255f, 0, 0, 0
                },
                {
                        ((float) sc.getGreen()) / 255f, 0, 0, 0
                },
                {
                        ((float) sc.getBlue()) / 255f, 0, 0, 0
                },
                {
                        0f, 0f, 0f, 1f
                }
        };
        BandCombineOp changeColors = new BandCombineOp(colorMatrix, null);
        Raster sourceRaster = src.getRaster();
        WritableRaster displayRaster = sourceRaster.createCompatibleWritableRaster();
        changeColors.filter(sourceRaster, displayRaster);
        return new BufferedImage(src.getColorModel(), displayRaster, true, null);
    }

    public static BufferedImage getFromClasspath(String path)
    {
        if(path == null || path.trim().equals(""))
            return null;
        try
        {
            if(!imgs.containsKey(path))
            {
                imgs.put(path, ImageIO.read(ImageUtils.class.getResourceAsStream(path)));
            }
            return imgs.get(path);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static ByteBuffer getPixels(BufferedImage img)
    {
        int w = img.getWidth();
        int h = img.getHeight();
        int[] pixels = img.getRGB(0, 0, w, h, null, 0, w);
        ByteBuffer pixelBuf = BufferUtils.createByteBuffer(pixels.length * 4);
        for(int y = 0; y < h; y++ )
        {
            for(int x = 0; x < w; x++ )
            {
                int color = pixels[x + y * w];
                int a = color >> 24 & 0xFF;
                int r = color >> 16 & 0xFF;
                int g = color >> 8 & 0xFF;
                int b = color >> 0 & 0xFF;
                pixelBuf.put((byte) r);
                pixelBuf.put((byte) g);
                pixelBuf.put((byte) b);
                pixelBuf.put((byte) a);
            }
        }
        pixelBuf.flip();
        return pixelBuf;
    }

    public static BufferedImage toBufferedImage(Icon icon)
    {
        BufferedImage result = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, result.createGraphics(), 0, 0);
        return result;
    }

    private static int offset;

    private static int btoi(byte b)
    {
        int a = b;
        return (a < 0 ? 256 + a : a);
    }

    private static int read(byte[] buf)
    {
        return btoi(buf[offset++ ]);
    }

    /**
     * Creates a BufferedImage from given TGA data
     */
    public static BufferedImage decodeTGA(byte[] buf) throws IOException
    {
        offset = 0;

        // Reading header bytes
        // buf[2]=image type code 0x02=uncompressed BGR or BGRA
        // buf[12]+[13]=width
        // buf[14]+[15]=height
        // buf[16]=image pixel size 0x20=32bit, 0x18=24bit
        // buf{17]=Image Descriptor Byte=0x28 (00101000)=32bit/origin
        // upperleft/non-interleaved
        for(int i = 0; i < 12; i++ )
            read(buf);
        int width = read(buf) + (read(buf) << 8); // 00,04=1024
        int height = read(buf) + (read(buf) << 8); // 40,02=576
        read(buf);
        read(buf);

        int n = width * height;
        int[] pixels = new int[n];
        int idx = 0;

        if(buf[2] == 0x02 && buf[16] == 0x20)
        { // uncompressed BGRA
            while(n > 0)
            {
                int b = read(buf);
                int g = read(buf);
                int r = read(buf);
                int a = read(buf);
                int v = (a << 24) | (r << 16) | (g << 8) | b;
                pixels[idx++ ] = v;
                n -= 1;
            }
        }
        else if(buf[2] == 0x02 && buf[16] == 0x18)
        { // uncompressed BGR
            while(n > 0)
            {
                int b = read(buf);
                int g = read(buf);
                int r = read(buf);
                int a = 255; // opaque pixel
                int v = (a << 24) | (r << 16) | (g << 8) | b;
                pixels[idx++ ] = v;
                n -= 1;
            }
        }
        else
        {
            // RLE compressed
            while(n > 0)
            {
                int nb = read(buf); // num of pixels
                if((nb & 0x80) == 0)
                { // 0x80=dec 128, bits 10000000
                    for(int i = 0; i <= nb; i++ )
                    {
                        int b = read(buf);
                        int g = read(buf);
                        int r = read(buf);
                        pixels[idx++ ] = 0xff000000 | (r << 16) | (g << 8) | b;
                    }
                }
                else
                {
                    nb &= 0x7f;
                    int b = read(buf);
                    int g = read(buf);
                    int r = read(buf);
                    int v = 0xff000000 | (r << 16) | (g << 8) | b;
                    for(int i = 0; i <= nb; i++ )
                        pixels[idx++ ] = v;
                }
                n -= nb + 1;
            }
        }

        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bimg.setRGB(0, 0, width, height, pixels, 0, width);
        return bimg;
    }

    public static BufferedImage loadImage(AbstractResource res) throws IOException
    {
        if(imagesMap.containsKey(res))
            return imagesMap.get(res);
        BufferedImage img = null;
        if(res.getResourceLocation().getExtension().equalsIgnoreCase("tga"))
        {
            img = ImageUtils.decodeTGA(res.getData());
        }
        else
            img = ImageIO.read(new ByteArrayInputStream(res.getData()));
        imagesMap.put(res, img);
        return img;
    }

    public static BufferedImage resize(BufferedImage img, int w, int h)
    {
        BufferedImage resized = new BufferedImage(w, h, img.getType());
        Graphics g = resized.createGraphics();
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();
        return resized;
    }
}
