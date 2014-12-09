package org.craft.entity;

import com.mojang.nbt.*;

import org.craft.world.*;

public class EntityPrimedTNT extends Entity
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
            Explosion explosion = new Explosion(worldObj, posX, posY, posZ, 4f);
            explosion.producesSmoke(true);
            explosion.perform();
            setDead();
        }
    }

    public void writeToNBT(NBTCompoundTag compound)
    {
        super.writeToNBT(compound);
        compound.putLong("fuse", fuse);
    }

}
