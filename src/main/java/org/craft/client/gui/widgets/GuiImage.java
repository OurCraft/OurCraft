package org.craft.client.gui.widgets;

import java.io.*;

import org.craft.client.*;
import org.craft.client.gui.*;
import org.craft.client.render.*;
import org.craft.client.render.texture.Texture;
import org.craft.client.render.texture.TextureRegion;
import org.craft.resources.*;

public class GuiImage extends GuiWidget
{

    private TextureRegion region;

    public GuiImage(int id, int x, int y, ResourceLocation location) throws IOException
    {
        super(id, x, y, 0, 0);
        Texture texture = OpenGLHelper.loadTexture(OurCraft.getOurCraft().getAssetsLoader().getResource(location));
        this.region = new TextureRegion(texture);
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
    }

    public GuiImage(int id, int x, int y, int w, int h, ResourceLocation location) throws IOException
    {
        this(id, x, y, w, h, OpenGLHelper.loadTexture(OurCraft.getOurCraft().getAssetsLoader().getResource(location)));
    }

    public GuiImage(int id, int x, int y, int w, int h, Texture texture)
    {
        this(id, x, y, w, h, new TextureRegion(texture));
    }

    public GuiImage(int id, int x, int y, int w, int h, TextureRegion textureRegion)
    {
        super(id, x, y, w, h);
        this.region = textureRegion;
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        engine.bindTexture(region.getTexture());
        Gui.drawTexturedRect(engine, getX(), getY(), getWidth(), getHeight(), region.getMinU(), region.getMinV(), region.getMaxU(), region.getMaxV());
    }

    public TextureRegion getRegion()
    {
        return region;
    }

}
