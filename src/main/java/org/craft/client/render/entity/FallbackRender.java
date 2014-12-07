package org.craft.client.render.entity;

import java.io.*;

import org.craft.client.*;
import org.craft.client.models.*;
import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.resources.*;

public class FallbackRender<T extends Entity> extends ModelRender<T>
{

    private static ModelBase fallbackModel;
    private static Texture   baseTexture;

    public FallbackRender()
    {
        super(createFallbackModel());
        if(baseTexture == null)
        {
            try
            {
                baseTexture = OpenGLHelper.loadTexture(OurCraft.getOurCraft().getAssetsLoader().getResource(new ResourceLocation("ourcraft", "textures/entities/test.png")));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static ModelBase createFallbackModel()
    {
        if(fallbackModel == null)
        {
            fallbackModel = new ModelBase();
            ModelBox box = fallbackModel.addBox(-0.5f, 0, -0.5f, 1, 1, 1);
            box.setPixelRatio(8f);
        }
        return fallbackModel;
    }

    @Override
    public Texture getTexture(Entity e)
    {
        return baseTexture;
    }

}
