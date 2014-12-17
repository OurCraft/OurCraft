package org.craft.spoonge.entity;

import org.spongepowered.api.entity.*;

public class SpoongeEntitySnapshot implements EntitySnapshot
{

    private Entity     entity;
    private EntityType type;

    public SpoongeEntitySnapshot(EntityType type, Entity e)
    {
        this.entity = e;
        this.type = type;
    }

    @Override
    public EntityType getType()
    {
        return type;
    }

    @Override
    public EntitySnapshot getSnapshot()
    {
        return this;
    }

}
