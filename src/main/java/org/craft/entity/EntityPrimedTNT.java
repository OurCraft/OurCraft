package org.craft.entity;

import com.mojang.nbt.*;

import org.craft.world.*;

public class EntityPrimedTNT extends Entity implements IExplosiveEntity
{

    private long fuse;

    public EntityPrimedTNT(World world)
    {
        super(world);
    }

    public void setFuse(long fuse)
    {
        this.fuse = fuse;
    }

    public long getFuse()
    {
        return fuse;
    }

    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        fuse-- ;
        if(fuse <= 0)
        {
            Explosion explosion = new Explosion(this, 6f);
            explosion.producesSmoke(true);
            worldObj.performExplosion(explosion);
            setDead();
        }
    }

    public void readFromNBT(NBTCompoundTag compound)
    {
        super.readFromNBT(compound);
        fuse = compound.getLong("fuse");
    }

    public void writeToNBT(NBTCompoundTag compound)
    {
        super.writeToNBT(compound);
        compound.putLong("fuse", fuse);
    }

    @Override
    public void onExplosion(Explosion explosion, World world, float x, float y, float z)
    {
        ;
    }

}
