package org.craft.client.render;

import java.awt.image.*;
import java.io.*;
import java.util.*;

import org.craft.client.*;
import org.craft.resources.*;
import org.craft.utils.*;

public class ColorPalette
{

    public static ColorPalette NES_PALETTE;

    public static ColorPalette APPLE_II;
    public static ColorPalette AMSTRAD_CPC;
    public static ColorPalette CGA;
    public static ColorPalette COMMODORE_PLUS_4;
    public static ColorPalette COMMODORE_64;
    public static ColorPalette COMMODORE_VIC20;
    public static ColorPalette DOOM;
    public static ColorPalette MSX;
    public static ColorPalette MSX2_PLUS;
    public static ColorPalette SAM_COUPÉ;
    public static ColorPalette ZX_SPECTRUM;
    public static ColorPalette NTSC;
    public static ColorPalette PAL;
    public static ColorPalette GAMEBOY;
    public static ColorPalette GAMEBOY_GRAY;

    private int[]              colors;

    private Shader             shader;

    public static void init(OurCraft craft)
    {
        NES_PALETTE = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/nes.png"));
        APPLE_II = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/appleII.png"));
        AMSTRAD_CPC = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/amstrad_cpc.png"));
        CGA = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/cga.png"));
        COMMODORE_PLUS_4 = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/c+4.png"));
        COMMODORE_64 = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/c64.png"));
        COMMODORE_VIC20 = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/cVIC20.png"));
        DOOM = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/doom.png"));
        MSX = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/MSX.png"));
        MSX2_PLUS = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/MSX2Plus.png"));
        SAM_COUPÉ = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/sam_coupé.png"));
        ZX_SPECTRUM = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/zx_spectrum.png"));
        NTSC = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/ntsc.png"));
        PAL = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/pal.png"));
        GAMEBOY = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/gameboy.png"));
        GAMEBOY_GRAY = createFromImage(craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/gameboy_gray.png"));
    }

    /**
     * @param colors
     */
    public ColorPalette(OurCraft craft, int[] colors)
    {
        this.colors = colors;
        try
        {
            String palette = createGLSLPalette();
            shader = new Shader(craft.getAssetsLoader().getResource(new ResourceLocation("ourcraft", "shaders/palette.vsh")).readContent().replace("#palette#", palette), craft.getAssetsLoader().getResource(new ResourceLocation("ourcraft", "shaders/palette.fsh")).readContent().replace("#palette#", palette));
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private String createGLSLPalette()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("palette[" + colors.length + "] = vec3[](");
        int index = 0;
        for(int color : colors)
        {
            if(index++ != 0)
                buffer.append(",");
            String vec3 = "vec3(";
            vec3 += ((double) ((color >> 16) & 0xFF) / 255.0);
            vec3 += "," + ((double) ((color >> 8) & 0xFF) / 255.0);
            vec3 += "," + ((double) ((color >> 0) & 0xFF) / 255.0);
            buffer.append(vec3).append(")");
        }
        buffer.append(")");
        String result = buffer.toString();
        //        System.out.println(result);
        return result;
    }

    public int[] getColors()
    {
        return colors;
    }

    public void bind()
    {
        if(shader != null)
        {
            this.shader.bind();
        }
    }

    public Shader getShader()
    {
        return shader;
    }

    public static ColorPalette createFromImage(OurCraft ourcraft, BufferedImage img)
    {
        ColorPalette palette = null;
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int[] colorsArray = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        for(int color : colorsArray)
            if(!colors.contains(new Integer(color)))
                colors.add(color);

        Integer[] array = colors.toArray(new Integer[0]);
        int[] array1 = new int[array.length];
        for(int i = 0; i < array.length; i++ )
            array1[i] = array[i];
        palette = new ColorPalette(ourcraft, array1);
        return palette;
    }
}
