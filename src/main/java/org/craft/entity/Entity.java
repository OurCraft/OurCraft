package org.craft.entity;

import java.util.*;

import org.craft.blocks.*;
import org.craft.maths.*;
import org.craft.spongeimpl.math.*;
import org.craft.utils.*;
import org.craft.world.*;
import org.spongepowered.api.math.*;

public class Entity implements org.spongepowered.api.entity.Entity
{

    public float              posX;
    public float              posY;
    public float              posZ;
    public float              velX;
    public float              velY;
    public float              velZ;
    public float              yaw;
    public float              pitch;
    public float              roll;

    public float              headYaw;
    public float              headPitch;
    public float              headRoll;

    public float              lastYaw;
    public float              lastPitch;

    public World              worldObj;
    private boolean           isDead;
    private AABB              boundingBox;
    public boolean            onGround;
    private Quaternion        rotationQuaternion;
    private boolean           onFire;
    private int               fireTicks;

    public static final float G = 9.81f / 360f;

    /**
     * Instantiates an Entity with given world
     */
    public Entity(World world)
    {
        this.boundingBox = new AABB(Vector3.get(0, 0, 0), Vector3.get(1, 1, 1));
        this.isDead = false;
        this.worldObj = world;
    }

    /**
     * Set entity's bounding box size
     */
    public void setSize(float width, float height, float depth)
    {
        this.boundingBox = new AABB(Vector3.get(0, 0, 0), Vector3.get(width, height, depth));
    }

    /**
     * Returns entity's bounding box in world space
     */
    public AABB getBoundingBox()
    {
        return boundingBox.translate(Vector3.get(posX, posY, posZ));
    }

    /**
     * Updates entity (gravity, movements)
     */
    public void update()
    {
        onGround = true;
        onEntityUpdate();

        velY += -G;

        if(velY < -G * 6)
        {
            velY = -G * 6;
        }

        if(canGo(posX + velX, posY, posZ))
        {
            posX += velX;
        }
        else
            velX = 0;
        if(velY == 0f)
        {
            ;
        }
        else
        {
            if(canGo(posX, posY + velY, posZ))
            {
                posY += velY;
                onGround = false;
            }
            else
            {
                velY = 0;
            }
        }
        if(canGo(posX, posY, posZ + velZ))
        {
            posZ += velZ;
        }
        else
            velZ = 0;

        if(pitch > Math.toRadians(90))
        {
            pitch = (float) Math.toRadians(90);
        }
        else if(pitch < Math.toRadians(-90))
        {
            pitch = (float) Math.toRadians(-90);
        }

        if(onGround)
        {
            velX *= 0.12f;
            velZ *= 0.12f;
        }
        else
        {
            velX *= 0.12f / 2f;
            velZ *= 0.12f / 2f;
        }
    }

    /**
     * Checks if entity can go at given position
     */
    private boolean canGo(float nx, float ny, float nz)
    {
        AABB boundingBox = this.boundingBox.translate(nx, ny, nz);
        Vector3 min = boundingBox.getMinExtents();
        Vector3 max = boundingBox.getMaxExtents();
        int startX = (int) Math.round(min.getX() - 0.5f);
        int startY = (int) Math.round(min.getY() - 0.5f);
        int startZ = (int) Math.round(min.getZ() - 0.5f);
        int endX = (int) Math.round(max.getX() + 0.5f);
        int endY = (int) Math.round(max.getY() + 0.5f);
        int endZ = (int) Math.round(max.getZ() + 0.5f);
        for(int x = startX; x <= endX; x++ )
        {
            for(int y = startY; y <= endY; y++ )
            {
                for(int z = startZ; z <= endZ; z++ )
                {
                    Block block = worldObj.getBlock(x, y, z);
                    if(block == null)
                        continue;
                    AABB blockBB = block.getCollisionBox(worldObj, x, y, z);
                    if(blockBB == null)
                        continue;
                    IntersectionInfos interInfos = boundingBox.intersectAABB(blockBB);
                    if(interInfos.doesIntersects() || interInfos.getDistance() <= 0.10f)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public float getEyeOffset()
    {
        return 0.0f;
    }

    /**
     * Teleport entity to given coordinates
     */
    public void setLocation(float x, float y, float z)
    {
        posX = x;
        posY = y;
        posZ = z;
    }

    public void moveBackwards(float distance)
    {
        moveForward(-distance);
    }

    public void moveForward(float distance)
    {
        velX += (float) (Math.sin(yaw) * distance);
        velZ += (float) (Math.cos(yaw) * distance);
    }

    public void moveLeft(float distance)
    {
        moveRight(-distance);
    }

    public void moveRight(float distance)
    {
        velX += (float) (Math.sin(yaw + Math.toRadians(90)) * distance);
        velZ += (float) (Math.cos(yaw + Math.toRadians(90)) * distance);
    }

    public Quaternion getQuaternionRotation()
    {
        if(rotationQuaternion == null || hasRotationChanged())
        {
            rotationQuaternion = new Quaternion(Vector3.yAxis, this.getYaw()).mul(new Quaternion(Vector3.xAxis, this.getPitch()))/* .mul(new Quaternion(Vector3.zAxis, this.getRoll())) */;
        }
        return rotationQuaternion;
    }

    private boolean hasRotationChanged()
    {
        if(lastPitch != pitch || lastYaw != yaw)
        {
            lastPitch = pitch;
            lastYaw = pitch;
            return true;
        }
        return false;
    }

    /**
     * Updates the isDead flag in order to make the world despawn this entity
     */
    public void setDead()
    {
        this.isDead = false;
    }

    /**
     * Updates Entity AI
     */
    public void onEntityUpdate()
    {

    }

    public float getYaw()
    {
        return yaw;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getRoll()
    {
        return roll;
    }

    public World getWorld()
    {
        return worldObj;
    }

    public float getX()
    {
        return posX;
    }

    public float getY()
    {
        return posY;
    }

    public float getZ()
    {
        return posZ;
    }

    public boolean isDead()
    {
        return isDead;
    }

    /**
     * Makes the entity jump
     */
    public void jump()
    {
        if(onGround)
        {
            velY = 0.25f;
        }
    }

    public boolean isOnGround()
    {
        return onGround;
    }

    /**
     * Performs a raycast to get object in front of entity
     */
    public CollisionInfos getObjectInFront(float maxDist)
    {
        CollisionInfos infos = new CollisionInfos();
        worldObj.performRayCast(this, infos, maxDist);
        return infos;
    }

    /**
     * Returns distance in meters between this entity and given one
     */
    public float getDistance(Entity other)
    {
        float dx = other.getX() - posX;
        float dy = other.getY() - posY;
        float dz = other.getZ() - posZ;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public UUID getUniqueId()
    {
        // TODO
        return null;
    }

    @Override
    public boolean isBurning()
    {
        return onFire;
    }

    @Override
    public int getDuration()
    {
        return fireTicks;
    }

    @Override
    public void setDuration(int ticks)
    {
        this.fireTicks = ticks;
    }

    @Override
    public Vector3f getVelocity()
    {
        return new Vec3f(velX, velY, velZ);
    }

    @Override
    public void setVelocity(Vector3f velocity)
    {
        this.velX = velocity.getX();
        this.velY = velocity.getY();
        this.velZ = velocity.getZ();
    }

    @Override
    public Vector3d getPosition()
    {
        return new Vec3d(posX, posY, posZ);
    }

    @Override
    public void setPosition(Vector3d position)
    {
        posX = (float) position.getX();
        posY = (float) position.getY();
        posZ = (float) position.getZ();
    }

    @Override
    public Vector3f getVectorRotation()
    {
        // TODO
        return null;
    }

    @Override
    public void setVectorRotation(Vector3f rotation)
    {
        // TODO
    }

    @Override
    public EulerDirection getRotation()
    {
        return new SpongeEulerDirection(yaw, pitch, roll);
    }

    @Override
    public void setRotation(EulerDirection rotation)
    {
        this.yaw = rotation.getYaw();
        this.pitch = rotation.getPitch();
        this.roll = rotation.getRoll();
    }

}
