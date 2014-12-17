package org.craft.spoonge.modifiers;

import java.util.*;

import com.flowpowered.math.vector.*;
import com.google.common.base.Optional;

import org.craft.entity.*;
import org.craft.modding.modifiers.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.util.*;
import org.spongepowered.api.world.*;
import org.spongepowered.api.world.extent.*;

@BytecodeModifier("org.craft.entity.Entity")
public class SpoongeEntity implements Entity, EntityType
{

    @Shadow
    public org.craft.world.World worldObj;

    @Shadow
    public SpoongeEntity(org.craft.world.World w)
    {

    }

    // START OF SHADOW METHODS
    @Shadow
    public UUID getUUID()
    {
        return null;
    }

    @Shadow
    public float getPosX()
    {
        return 0;
    }

    @Shadow
    public float getPosY()
    {
        return 0;
    }

    @Shadow
    public float getPosZ()
    {
        return 0;
    }

    @Shadow
    public boolean isDead()
    {
        return false;
    }

    @Shadow
    public void setDead()
    {
        ;
    }

    @Shadow
    public boolean isOnGround()
    {
        return false;
    }

    @Shadow
    public void setLocation(float x, float y, float z)
    {
        ;
    }

    // END OF SHADOW METHODS
    @Override
    public UUID getUniqueId()
    {
        return getUUID();
    }

    @Override
    public EntityType getType()
    {
        return this;
    }

    @Override
    public EntitySnapshot getSnapshot()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> Optional<T> getData(Class<T> dataClass)
    {
        return Optional.absent();
    }

    @Override
    public void remove()
    {
        setDead();
    }

    @Override
    public void interact(EntityInteractionType interactionType)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void interactWith(ItemStack itemStack, EntityInteractionType interactionType)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Location getLocation()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void teleport(Location location)
    {
        Vector3f loc = location.getPosition().toFloat();
        setLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void teleport(Extent extent, Vector3d position)
    {
        Vector3f loc = position.toFloat();
        setLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public Vector3d getPosition()
    {
        return new Vector3d(getPosX(), getPosY(), getPosZ());
    }

    @Override
    public void setPosition(Vector3d position)
    {
        Vector3f loc = position.toFloat();
        setLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public World getWorld()
    {
        return (World) worldObj;
    }

    @Override
    public void teleport(Vector3d position, World world)
    {
        Vector3f loc = position.toFloat();
        setLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void teleport(double x, double y, double z, World world)
    {
        setLocation((float) x, (float) y, (float) z);
    }

    @Override
    public double getX()
    {
        return getPosX();
    }

    @Override
    public double getY()
    {
        return getPosY();
    }

    @Override
    public double getZ()
    {
        return getPosZ();
    }

    @Override
    public Vector3f getVectorRotation()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setVectorRotation(Vector3f rotation)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public EulerDirection getRotation()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRotation(EulerDirection rotation)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mount(Entity entity)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void dismount()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void eject()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<Entity> getRider()
    {
        return Optional.absent();
    }

    @Override
    public Optional<Entity> getRiding()
    {
        return Optional.absent();
    }

    @Override
    public float getBase()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getHeight()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getScale()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isValid()
    {
        return !isDead();
    }

    @Override
    public int getFireTicks()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setFireTicks(int ticks)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getFireDelay()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getId()
    {
        for(Class<? extends org.craft.entity.Entity> entityClass : EntityRegistry.getAllEntityTypes())
        {
            Class<?> c = entityClass;
            if(c == getClass())
            {
                return EntityRegistry.getType(entityClass);
            }
        }
        return null;
    }

}
