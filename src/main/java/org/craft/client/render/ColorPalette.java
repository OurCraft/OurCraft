package org.craft.client.render;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;

import com.google.common.collect.*;

import org.craft.client.*;
import org.craft.resources.*;
import org.craft.utils.*;

public class ColorPalette
{

    private static List<ColorPalette> palettes;
    public static ColorPalette        NES_PALETTE;

    public static ColorPalette        APPLE_II;
    public static ColorPalette        AMSTRAD_CPC;
    public static ColorPalette        CGA;
    public static ColorPalette        COMMODORE_PLUS_4;
    public static ColorPalette        COMMODORE_64;
    public static ColorPalette        COMMODORE_VIC20;
    public static ColorPalette        DOOM;
    public static ColorPalette        MSX;
    public static ColorPalette        MSX2_PLUS;
    public static ColorPalette        SAM_COUPÉ;
    public static ColorPalette        ZX_SPECTRUM;
    public static ColorPalette        NTSC;
    public static ColorPalette        PAL;
    public static ColorPalette        GAMEBOY;
    public static ColorPalette        GAMEBOY_GRAY;

    private int[]                     colors;

    private Shader                    shader;

    private String                    name;

    public static void init(OurCraft craft)
    {
        palettes = Lists.newArrayList();
        NES_PALETTE = createFromImage("NES", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/nes.png"));
        APPLE_II = createFromImage("Apple II", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/appleII.png"));
        AMSTRAD_CPC = createFromImage("AMSTRAD CPC", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/amstrad_cpc.png"));
        CGA = createFromImage("CGA", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/cga.png"));
        COMMODORE_PLUS_4 = createFromImage("Commodore+4", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/c+4.png"));
        COMMODORE_64 = createFromImage("Commodore64", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/c64.png"));
        COMMODORE_VIC20 = createFromImage("Commodore Vic20", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/cVIC20.png"));
        DOOM = createFromImage("Doom", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/doom.png"));
        MSX = createFromImage("MSX", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/MSX.png"));
        MSX2_PLUS = createFromImage("MSX2+", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/MSX2Plus.png"));
        SAM_COUPÉ = createFromImage("SAM COUPÉ", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/sam_coupé.png"));
        ZX_SPECTRUM = createFromImage("ZX SPECTRUM", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/zx_spectrum.png"));
        NTSC = createFromImage("NTSC", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/ntsc.png"));
        PAL = createFromImage("PAL", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/pal.png"));
        GAMEBOY = createFromImage("GameBoy", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/gameboy.png"));
        GAMEBOY_GRAY = createFromImage("GameBoy - Gray", craft, ImageUtils.getFromClasspath("/assets/ourcraft/palettes/gameboy_gray.png"));
    }

    /**
     * @param colors
     */
    public ColorPalette(OurCraft craft, String name, int[] colors)
    {
        palettes.add(this);
        this.colors = colors;
        this.name = name;
        try
        {
            String palette = createGLSLPalette();
            String hsbPalette = createPaletteHSB();
            shader = new Shader(name, craft.getAssetsLoader().getResource(new ResourceLocation("ourcraft", "shaders/palette.vsh")).readContent().replace("#palette#", palette).replace("#hsbcolors#", hsbPalette), craft.getAssetsLoader().getResource(new ResourceLocation("ourcraft", "shaders/palette.fsh")).readContent().replace("#palette#", palette).replace("#hsbcolors#", hsbPalette));
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

    public String getName()
    {
        return name;
    }

    private String createPaletteHSB()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("hsbcolors[" + colors.length + "] = vec3[](");
        int index = 0;
        for(int color : colors)
        {
            if(index++ != 0)
                buffer.append(",");
            String vec3 = "vec3(";
            double r = ((double) ((color >> 16) & 0xFF) / 255.0);
            double g = ((double) ((color >> 8) & 0xFF) / 255.0);
            double b = ((double) ((color >> 0) & 0xFF) / 255.0);
            float[] vals = Color.RGBtoHSB((int) (r * 255), (int) (g * 255), (int) (b * 255), null);
            vec3 += vals[0];
            vec3 += "," + vals[1];
            vec3 += "," + vals[2];
            buffer.append(vec3).append(")");
        }
        buffer.append(")");
        String result = buffer.toString();
        return result;
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

    public static ColorPalette createFromImage(String name, OurCraft ourcraft, BufferedImage img)
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
        palette = new ColorPalette(ourcraft, name, array1);
        return palette;
    }

    public static ColorPalette fromName(String name)
    {
        for(ColorPalette palette : palettes)
        {
            if(palette.name.equals(name))
                return palette;
        }
        return null;
    }

    public static List<ColorPalette> getPalettes()
    {
        return palettes;
    }
}
