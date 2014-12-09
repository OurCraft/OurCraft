package org.craft.client.render.entity;

import java.io.*;

import org.craft.client.*;
import org.craft.client.models.entities.*;
import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.resources.*;

public class RenderPrimedTNT extends ModelRender<Entity>
{

    private Texture text;

    public RenderPrimedTNT()
    {
        super(new TNTModel());
        try
        {
            text = OpenGLHelper.loadTexture(OurCraft.getOurCraft().getAssetsLoader().getResource(new ResourceLocation("ourcraft", "textures/entities/tnt.png")));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Texture getTexture(Entity e)
    {
        return text;
    }

}
