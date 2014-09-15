package org.craft.client;

import java.awt.image.*;
import java.nio.*;

import org.lwjgl.*;

public class OpenGLHelper
{

    public static Texture loadTexture(BufferedImage img)
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
                float a = ((color >> 24) & 0xFF) / 255f;
                float r = ((color >> 16) & 0xFF) / 255f;
                float g = ((color >> 8) & 0xFF) / 255f;
                float b = ((color >> 0) & 0xFF) / 255f;
                pixelBuf.put((byte)(r * 255f));
                pixelBuf.put((byte)(g * 255f));
                pixelBuf.put((byte)(b * 255f));
                pixelBuf.put((byte)(a * 255f));
            }
        }
        pixelBuf.flip();
        return new Texture(w, h, pixelBuf);
    }
}
