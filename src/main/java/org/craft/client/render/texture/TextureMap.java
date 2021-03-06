package org.craft.client.render.texture;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.*;

import com.google.common.collect.*;
import com.google.gson.*;

import org.craft.client.*;
import org.craft.client.render.AnimatedIcon;
import org.craft.client.render.ITickable;
import org.craft.client.render.IconGenerator;
import org.craft.client.render.Stitcher;
import org.craft.resources.*;
import org.craft.utils.*;

public class TextureMap implements IconGenerator, ITextureObject, IDisposable, ITickable
{

    private ResourceLoader         loader;
    private ResourceLocation       base;
    private Texture                texture;
    private BufferedImage          nullImage;
    private BufferedImage          emptyImage;
    private boolean                forceResize;
    private Stitcher               stitcher;
    private List<TextureMapSprite> registredSprites;

    /**
     * Creates TextureMap with given loader and base
     */
    public TextureMap(ResourceLoader loader, ResourceLocation base)
    {
        this(loader, base, false);
    }

    /**
     * Creates TextureMap with given loader and base
     */
    public TextureMap(ResourceLoader loader, ResourceLocation base, boolean resize)
    {
        this(loader, base, resize, false);
    }

    public TextureMap(ResourceLoader loader, ResourceLocation base, boolean resize, boolean putInCorner)
    {
        this.forceResize = resize;
        this.loader = loader;
        this.base = base;

        registredSprites = Lists.newArrayList();

        initNullAndEmptyImages();
        stitcher = new Stitcher(emptyImage, putInCorner);
    }

    /**
     * Completes given ResourceLocation to get full ResourceLocation from base
     */
    public ResourceLocation completeLocation(ResourceLocation loc)
    {
        ResourceLocation newLoc = new ResourceLocation(base.getFullPath(), loc.getFullPath());
        return newLoc;
    }

    /**
     * Instantiates nullImage and emptyImage
     */
    private void initNullAndEmptyImages()
    {
        if(loader.doesResourceExists(completeLocation(new ResourceLocation("missigno.png"))))
        {
            try
            {
                nullImage = ImageUtils.loadImage(loader.getResource(completeLocation(new ResourceLocation("missigno.png"))));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            nullImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics g = nullImage.createGraphics();
            for(int x = 0; x < 16; x++ )
            {
                for(int y = 0; y < 16; y++ )
                {
                    int color = 0xFF000000;
                    if((x >= 8 && y >= 8) || (x < 8 && y < 8))
                        color = 0xFFFF00DC;
                    nullImage.setRGB(x, y, color);
                }
            }
            g.dispose();

        }

        if(loader.doesResourceExists(completeLocation(new ResourceLocation(".png"))))
        {
            try
            {
                emptyImage = ImageUtils.loadImage(loader.getResource(completeLocation(new ResourceLocation(".png"))));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            emptyImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics g = emptyImage.createGraphics();
            for(int x = 0; x < 16; x++ )
            {
                for(int y = 0; y < 16; y++ )
                {
                    if(x == 0 || y == 0)
                        emptyImage.setRGB(x, y, 0xFF4800FF);
                    else
                        emptyImage.setRGB(x, y, 0xFFFF00DC);
                }
            }
            g.dispose();
        }
    }

    @Override
    public TextureIcon generateIcon(ResourceLocation loc)
    {
        for(TextureMapSprite sprite : registredSprites)
        {
            if(!sprite.useRawImage && sprite.location.equals(loc))
                return sprite.icon;
        }
        ResourceLocation metaLoc = new ResourceLocation(completeLocation(loc).getFullPath() + ".json");
        if(loader.doesResourceExists(metaLoc))
        {
            try
            {
                Log.message("ANIM >> " + metaLoc.getFullPath());
                String jsonData = new String(loader.getResource(metaLoc).getData(), "UTF-8");
                Gson gson = new Gson();
                JsonObject object = gson.fromJson(jsonData, JsonObject.class);
                if(object.has("animation"))
                {
                    JsonObject animData = object.get("animation").getAsJsonObject();
                    long totalTime = animData.get("totalTime").getAsLong();
                    int w = animData.get("tileWidth").getAsInt();
                    int h = animData.get("tileHeight").getAsInt();
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(loader.getResource(completeLocation(loc)).getData()));
                    int xNbr = image.getWidth() / w;
                    int yNbr = image.getHeight() / h;
                    TextureIcon[] tiles = new TextureIcon[xNbr * yNbr];
                    Log.message("w: " + w + ", h: " + h);
                    for(int y = 0; y < yNbr; y++ )
                    {
                        for(int x = 0; x < xNbr; x++ )
                        {
                            int xIndex = x * w;
                            int yIndex = y * h;

                            tiles[x + y * xNbr] = generateIcon(image.getSubimage(xIndex, yIndex, w, h));
                        }
                    }
                    TextureAnimatedSprite sprite = new TextureAnimatedSprite(totalTime);
                    AnimatedIcon animatedIcon = new AnimatedIcon(tiles);
                    sprite.location = loc;
                    sprite.icon = animatedIcon;
                    registredSprites.add(sprite);
                    return animatedIcon;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        TextureMapSprite sprite = new TextureMapSprite();
        TextureMapIcon icon = new TextureMapIcon(0, 0, 0, 0, 0, 0);
        sprite.location = loc;
        sprite.icon = icon;
        registredSprites.add(sprite);
        return icon;
    }

    /**
     * Generates an icon from given image
     */
    public TextureIcon generateIcon(BufferedImage img)
    {
        for(TextureMapSprite sprite : registredSprites)
        {
            if(sprite.useRawImage && sprite.rawImage.equals(img))
                return sprite.icon;
        }
        TextureMapSprite sprite = new TextureMapSprite();
        TextureMapIcon icon = new TextureMapIcon(0, 0, 0, 0, 0, 0);
        sprite.rawImage = img;
        sprite.icon = icon;
        sprite.useRawImage = true;
        registredSprites.add(sprite);
        return icon;
    }

    /**
     * Compiles the TextureMap to create icons from given images
     */
    public void compile() throws Exception
    {
        HashMap<Integer, TextureIcon> indexes = new HashMap<Integer, TextureIcon>();
        for(int i = 0; i < registredSprites.size(); i++ )
        {
            TextureMapSprite sprite = registredSprites.get(i);

            TextureIcon icon = sprite.icon;
            BufferedImage img = null;
            String name = null;
            if(!sprite.useRawImage)
            {
                ResourceLocation loc = completeLocation(sprite.location);
                try
                {
                    name = loc.getName();
                    AbstractResource res = loader.getResource(loc);
                    img = ImageUtils.loadImage(res);
                }
                catch(Exception e)
                {
                    Log.error("Unable to find icon: /" + loc.getFullPath());
                    img = nullImage;
                }
            }
            else
            {
                name = sprite.rawImage.toString();
                img = sprite.rawImage;
            }
            if(!(sprite instanceof TextureAnimatedSprite))
            {
                indexes.put(stitcher.addImage(img, name, forceResize), icon);
            }
            else
                indexes.put(-1, icon);
        }

        BufferedImage stitchedImage = stitcher.stitch();
        Iterator<Integer> indexesIt = indexes.keySet().iterator();
        while(indexesIt.hasNext())
        {
            int index = indexesIt.next();
            if(index >= 0)
            {
                TextureMapSprite sprite = registredSprites.get(index);
                TextureMapIcon icon = (TextureMapIcon) sprite.icon;
                icon.setMinU(stitcher.getMinU(index));
                icon.setMinV(stitcher.getMinV(index));
                icon.setMaxU(stitcher.getMaxU(index));
                icon.setMaxV(stitcher.getMaxV(index));
                icon.setWidth(stitcher.getWidth(index));
                icon.setHeight(stitcher.getHeight(index));
            }
        }

        if(Dev.debug())
        {
            if(stitchedImage != null)
            {
                try
                {
                    ImageIO.write(stitchedImage, "png", new File(Dev.getFolder(), StringUtils.createCorrectedFileName(this.base.getFullPath()) + ".png"));
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        texture = OpenGLHelper.loadTexture(stitchedImage);
    }

    /**
     * Gets a texture icon from given name or null if none exists with given name
     */
    public TextureIcon get(String name)
    {
        return get(new ResourceLocation(name));
    }

    /**
     * Gets a texture icon from given location or null if none exists with given location
     */
    public TextureIcon get(ResourceLocation loc)
    {
        for(TextureMapSprite sprite : registredSprites)
        {
            if(!sprite.useRawImage && sprite.location.equals(loc))
            {
                return sprite.icon;
            }
        }
        return TextureIcon.NULL_ICON;
    }

    /**
     * Returns generated texture
     */
    public Texture getTexture()
    {
        return texture;
    }

    /**
     * Sets tile size
     */
    public void setTileDimensions(int w, int h)
    {
        stitcher.setTileWidth(w);
        stitcher.setTileHeight(h);
    }

    /**
     * Gets tile width
     */
    public int getTileWidth()
    {
        return stitcher.getTileWidth();
    }

    /**
     * Gets tile height
     */
    public int getTileHeight()
    {
        return stitcher.getTileHeight();
    }

    private class TextureMapIcon implements TextureIcon
    {
        private float minu;
        private float maxu;
        private float minv;
        private float maxv;
        private int   width;
        private int   height;

        TextureMapIcon(float minu, float minv, float maxu, float maxv, int width, int height)
        {
            this.minu = minu;
            this.maxu = maxu;
            this.minv = minv;
            this.maxv = maxv;
            this.width = width;
            this.height = height;
        }

        @Override
        public float getWidth()
        {
            return width;
        }

        @Override
        public float getHeight()
        {
            return height;
        }

        @Override
        public float getMinU()
        {
            return minu;
        }

        @Override
        public float getMaxU()
        {
            return maxu;
        }

        @Override
        public float getMinV()
        {
            return minv;
        }

        @Override
        public float getMaxV()
        {
            return maxv;
        }

        public void setMinU(float minu)
        {
            this.minu = minu;
        }

        public void setMaxU(float maxu)
        {
            this.maxu = maxu;
        }

        public void setMinV(float minv)
        {
            this.minv = minv;
        }

        public void setMaxV(float maxv)
        {
            this.maxv = maxv;
        }

        public void setWidth(int width)
        {
            this.width = width;
        }

        public void setHeight(int height)
        {
            this.height = height;
        }
    }

    @Override
    public TextureIcon generateIcon(String loc)
    {
        return generateIcon(new ResourceLocation(loc + ".png"));
    }

    @Override
    public void bind()
    {
        texture.bind();
    }

    @Override
    public void dispose()
    {
        texture.dispose();
    }

    @Override
    public void tick()
    {
        for(TextureMapSprite sprite : registredSprites)
        {
            sprite.tick();
            if(sprite instanceof TextureAnimatedSprite)
            {
                TextureAnimatedSprite animated = (TextureAnimatedSprite) sprite;
                animated.activate();
            }
        }
    }

    public TextureMapSprite getSprite(TextureIcon textureIcon)
    {
        for(TextureMapSprite sprite : registredSprites)
        {
            if(sprite.icon == textureIcon)
                return sprite;
        }
        return null;
    }

}
