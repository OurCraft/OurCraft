package org.craft.client.models.entities;

import org.craft.client.models.*;

public class TNTModel extends ModelBase
{

    public TNTModel()
    {
        super();
        ModelBox box = new ModelBox(-0.5f, 1f, -0.5f, 1f, 1f, 1f);
        box.setPixelRatio(8f);
        box.setTextureSize(32f, 16f);
        //        box.setRotationPoint(0.5f, 0.5f, 0.5f);
        addBox(box);
    }
}
