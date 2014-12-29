package org.craft.client.render.entity;

import java.io.*;

import org.craft.client.*;
import org.craft.client.models.entities.*;
import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.resources.*;

public class RenderPrimedTNT extends ModelRender
{

    private Texture text;
    private Texture whiteText;

    public RenderPrimedTNT()
    {
        super(new TNTModel());
        try
        {
            text = OpenGLHelper.loadTexture(OurCraft.getOurCraft().getAssetsLoader().getResource(new ResourceLocation("ourcraft", "textures/entities/tnt.png")));
            whiteText = Texture.createColoredRect(0xFFFFFFFF);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Texture getTexture(Entity e)
    {
        if(e instanceof EntityPrimedTNT)
        {
            EntityPrimedTNT tnt = (EntityPrimedTNT) e;
            // TODO: Better function
            if(tnt.getFuse() % 40 <= 14)
                return whiteText;
        }
        return text;
    }
}
