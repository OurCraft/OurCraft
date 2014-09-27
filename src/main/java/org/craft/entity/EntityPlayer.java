package org.craft.entity;

import org.craft.world.*;

public class EntityPlayer extends Entity
{
    public EntityPlayer(World world)
    {
        super(world);
        setSize(0.75f, 1.80f, 0.75f);
    }

    public float getEyeOffset()
    {
        return 1.7f;
    }
}
