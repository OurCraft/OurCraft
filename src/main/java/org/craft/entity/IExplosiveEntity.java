package org.craft.entity;

import org.craft.world.*;

public interface IExplosiveEntity
{
    void onExplosion(Explosion explosion, World world, float x, float y, float z);
}
