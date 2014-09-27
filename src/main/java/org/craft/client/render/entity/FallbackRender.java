package org.craft.client.render.entity;

import org.craft.client.models.*;
import org.craft.entity.*;

public class FallbackRender<T extends Entity> extends ModelRender<T>
{

    private static ModelBase fallbackModel;

    public FallbackRender()
    {
        super(createFallbackModel());
    }

    private static ModelBase createFallbackModel()
    {
        if(fallbackModel == null)
        {
            fallbackModel = new ModelBase();
            fallbackModel.addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1);
        }
        return fallbackModel;
    }

}
