package org.craft.spoonge.modifiers;

import java.util.*;

import com.flowpowered.math.vector.*;
import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.modding.modifiers.*;
import org.craft.spoonge.entity.*;
import org.craft.spoonge.util.*;
import org.craft.spoonge.world.*;
import org.spongepowered.api.block.*;
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
    public float                 yaw;

    @Shadow
    public float                 pitch;

    @Shadow
    public float                 roll;

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

    @Shadow
    public int getFireTicks()
    {
        return 0;
    }

    @Shadow
    public void setFire(int t)
    {
        ;
    }

    @Shadow
    public AABB getBoundingBox()
    {
        return null;
    }

    @Shadow
    public void onInteract(org.craft.inventory.Stack itemStack)
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
        return new SpoongeEntitySnapshot(this, this);
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
        interactWith(null, interactionType);
    }

    @Override
    public void interactWith(ItemStack itemStack, EntityInteractionType interactionType)
    {
        onInteract((org.craft.inventory.Stack) itemStack);
    }

    @Override
    public Location getLocation()
    {
        return new Location(new SpoongeExtent(new BlockLoc[0], Lists.<Entity> newArrayList(this)), new Vector3d(getPosX(), getPosY(), getPosZ()));
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
        return getRotation().toVector();
    }

    @Override
    public void setVectorRotation(Vector3f rotation)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public EulerDirection getRotation()
    {
        return new SpoongeEulerDirection(yaw, pitch, roll);
    }

    @Override
    public void setRotation(EulerDirection rotation)
    {
        yaw = rotation.getYaw();
        pitch = rotation.getPitch();
        roll = rotation.getRoll();
    }

    @Override
    public void mount(Entity entity)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dismount()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void eject()
    {
        throw new UnsupportedOperationException();
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
        float w = getBoundingBox().getMaxExtents().getX() - getBoundingBox().getMinExtents().getX();
        return w;
    }

    @Override
    public float getHeight()
    {
        float h = getBoundingBox().getMaxExtents().getY() - getBoundingBox().getMinExtents().getY();
        return h;
    }

    @Override
    public float getScale()
    {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public boolean isValid()
    {
        return !isDead();
    }

    @Override
    public void setFireTicks(int ticks)
    {
        setFire(ticks);
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
