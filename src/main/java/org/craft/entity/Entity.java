package org.craft.entity;

import java.util.*;

import com.mojang.nbt.*;

import org.craft.blocks.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;
import org.craft.world.*;

public abstract class Entity implements ILocatable
{

    public float           posX;
    public float           posY;
    public float           posZ;
    public float           velX;
    public float           velY;
    public float           velZ;
    public float           yaw;
    public float           pitch;
    public float           roll;

    public float           headYaw;
    public float           headPitch;
    public float           headRoll;

    public float           lastYaw;
    public float           lastPitch;

    public World           worldObj;
    private boolean        isDead;
    private AABB           boundingBox;
    public boolean         onGround;
    private Quaternion     rotationQuaternion;
    private boolean        onFire;
    private int            fireTicks;
    protected UUID         uuid;
    protected float        stepHeight;
    private boolean        wasOnGround;
    public int             entityID;
    private CollisionInfos collInfos;

    /**
     * Instantiates an Entity with given world
     */
    public Entity(World world)
    {
        collInfos = new CollisionInfos();
        uuid = generateUUID();
        this.boundingBox = new AABB(Vector3.get(0, 0, 0), Vector3.get(1, 1, 1));
        this.isDead = false;
        this.worldObj = world;
    }

    public UUID generateUUID()
    {
        return UUID.randomUUID();
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
    public void update(double delta)
    {
        float gravity = worldObj.getGravity();
        wasOnGround = onGround;
        onGround = true;

        if(canGo(posX + velX, posY, posZ))
        {
            posX += velX;
        }
        else if(canGo(posX + velX, posY + stepHeight, posZ) && wasOnGround)
        {
            velY = 0;
            posX += velX;
            posY += stepHeight;
        }
        else
            velX = 0;

        if(canGo(posX, posY, posZ + velZ))
        {
            posZ += velZ;
        }
        else if(canGo(posX, posY + stepHeight, posZ + velZ) && wasOnGround)
        {
            velY = 0;
            posZ += velZ;
            posY += stepHeight;
        }
        else
            velZ = 0;

        if(canGo(posX, posY + velY, posZ))
        {
            posY += velY;
            onGround = false;
        }
        else
        {
            velY = 0;
        }

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
            velX *= 0.20f;
            velZ *= 0.20f;
        }

        onEntityUpdate();
        velY -= gravity;

        if(velY < -gravity * 20)
        {
            velY = -gravity * 20;
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
                    if(worldObj.getChunk(x, y, z) == null)
                        return false;
                    Block block = worldObj.getBlockAt(x, y, z);
                    if(block == null)
                        continue;
                    AABB blockBB = block.getCollisionBox(worldObj, x, y, z);
                    if(blockBB == null)
                        continue;
                    if(boundingBox.intersectAABB(blockBB))
                    {
                        boundingBox.dispose();
                        blockBB.dispose();
                        return false;
                    }
                    blockBB.dispose();
                }
            }
        }
        boundingBox.dispose();
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

    public void setLocation(ILocatable location)
    {
        posX = location.getPosX();
        posY = location.getPosY();
        posZ = location.getPosZ();
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
        this.isDead = true;
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

    public float getPosX()
    {
        return posX;
    }

    public float getPosY()
    {
        return posY;
    }

    public float getPosZ()
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
        collInfos.type = CollisionType.NONE;
        worldObj.performRayCast(this, collInfos, maxDist);
        return collInfos;
    }

    /**
     * Returns distance in meters between this entity and given one
     */
    public float getDistance(Entity other)
    {
        float dx = (float) (other.getPosX() - posX);
        float dy = (float) (other.getPosY() - posY);
        float dz = (float) (other.getPosZ() - posZ);
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Reads all data from the compound to update/init this entity
     */
    public void readFromNBT(NBTCompoundTag compound)
    {
        if(compound.contains("uuid"))
            uuid = UUID.fromString(compound.getString("uuid"));
        posX = compound.getFloat("posX");
        posY = compound.getFloat("posY");
        posZ = compound.getFloat("posZ");
        velX = compound.getFloat("motionX");
        velY = compound.getFloat("motionY");
        velZ = compound.getFloat("motionZ");

        yaw = compound.getFloat("yaw");
        pitch = compound.getFloat("pitch");
        roll = compound.getFloat("roll");

        lastYaw = compound.getFloat("lastYaw");
        lastPitch = compound.getFloat("lastPitch");

        onFire = compound.getBoolean("onFire");
        fireTicks = compound.getInt("fireTicks");

        stepHeight = compound.getFloat("stepHeight");

        isDead = compound.getBoolean("dead");
    }

    /**
     * Writes all data belonging to this entity into the compound for later use
     */
    public void writeToNBT(NBTCompoundTag compound)
    {
        if(uuid != null)
            compound.putString("uuid", uuid.toString());
        compound.putString("typeID", EntityRegistry.getType(this));
        compound.putFloat("posX", posX);
        compound.putFloat("posY", posY);
        compound.putFloat("posZ", posZ);
        compound.putFloat("motionX", velX);
        compound.putFloat("motionY", velY);
        compound.putFloat("motionZ", velZ);

        compound.putFloat("yaw", yaw);
        compound.putFloat("pitch", pitch);
        compound.putFloat("roll", roll);

        compound.putFloat("lastYaw", lastYaw);
        compound.putFloat("lastPitch", lastPitch);

        compound.putBoolean("onFire", onFire);
        compound.putInt("fireTicks", fireTicks);

        compound.putFloat("stepHeight", stepHeight);

        compound.putBoolean("dead", isDead);
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public int getFireTicks()
    {
        return fireTicks;
    }

    public void setFire(int ticks)
    {
        this.fireTicks = ticks;
    }

    public void onInteract(org.craft.inventory.Stack itemStack)
    {
        ;
    }
}
