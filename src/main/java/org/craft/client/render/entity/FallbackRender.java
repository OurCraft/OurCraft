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
            ModelBox box = fallbackModel.addBox(0, 0, 0, 1, 1, 1);
            box.setRotationPoint(0.5f, 0.5f, 0.5f);
        }
        return fallbackModel;
    }

}
