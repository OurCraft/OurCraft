package org.craft.entity;

import com.mojang.nbt.*;

import org.craft.utils.*;
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
            Log.message("KABOOM");

            /* float r = 2f;
             for(int s = 0; s < 360; s += 40)
             {
                 for(int t = 0; t < 360; t += 40)
                 {
                     float x = (float) posX;
                     float y = (float) posY;
                     float z = (float) posZ;
                     x += r * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)) + Math.random();
                     y += r * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)) + Math.random();
                     z += r * Math.cos(Math.toRadians(t)) + Math.random();
                     worldObj.spawnParticle("test", x, y, z);
                 }
             }*/
            Explosion explosion = new Explosion(worldObj, posX, posY, posZ, 6f);
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
