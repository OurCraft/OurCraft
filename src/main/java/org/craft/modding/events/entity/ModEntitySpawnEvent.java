package org.craft.modding.events.entity;

import org.craft.*;
import org.craft.entity.*;
import org.craft.world.*;

public class ModEntitySpawnEvent extends ModEntityEvent implements ILocatable
{

    private World world;
    private float posX;
    private float posY;
    private float posZ;

    public ModEntitySpawnEvent(OurCraftInstance instance, Entity e, ILocatable location)
    {
        super(instance, e);
        this.world = location.getWorld();
        this.posX = location.getPosX();
        this.posY = location.getPosY();
        this.posZ = location.getPosZ();
    }

    public ModEntitySpawnEvent(OurCraftInstance instance, Entity e, World w, float x, float y, float z)
    {
        super(instance, e);
        this.world = w;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    @Override
    public boolean isCancellable()
    {
        return true;
    }

    @Override
    public World getWorld()
    {
        return world;
    }

    @Override
    public float getPosX()
    {
        return posX;
    }

    @Override
    public float getPosY()
    {
        return posY;
    }

    @Override
    public float getPosZ()
    {
        return posZ;
    }

}
