package org.craft.entity;

import org.craft.blocks.*;
import org.craft.maths.*;
import org.craft.utils.*;
import org.craft.world.*;

public class Entity
{

    private Vector3           pos;
    private Vector3           lastTickPos;
    private Vector3           velocity;
    private Quaternion        rotation;
    private Quaternion        headRotation;
    private World             worldObj;
    private boolean           isDead;
    private AABB              boundingBox;
    private boolean           onGround;

    public static final float G = 9.81f / 360f;

    /**
     * Instantiates an Entity with given world
     */
    public Entity(World world)
    {
        this.boundingBox = new AABB(Vector3.get(0, 0, 0), Vector3.get(1, 1, 1));
        this.pos = Vector3.get(0, 0, 0);
        lastTickPos = pos.copy();
        this.velocity = Vector3.NULL;
        this.isDead = false;
        this.worldObj = world;
        rotation = new Quaternion();
        headRotation = new Quaternion();
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
        return boundingBox.translate(pos);
    }

    /**
     * Updates entity (gravity, movements)
     */
    public void update()
    {
        onGround = true;
        onEntityUpdate();

        velocity = velocity.add(0, -G, 0);

        if(velocity.getY() < -G * 6)
        {
            velocity.setY(-G * 6);
        }

        if(canGo(pos.add(velocity.getX(), 0, 0)))
        {
            pos = pos.add(velocity.getX(), 0, 0);
        }
        else
            velocity.setX(0);
        if(velocity.y == 0f)
        {
            ;
        }
        else
        {
            if(canGo(pos.add(0, velocity.getY(), 0)))
            {
                pos = pos.add(0, velocity.getY(), 0);
                onGround = false;
            }
            else
            {
                velocity.setY(0);
            }
        }
        if(canGo(pos.add(0, 0, velocity.getZ())))
        {
            pos = pos.add(0, 0, velocity.getZ());
        }
        else
            velocity.setZ(0);

        IntersectionInfos intersectionWithGround = getBoundingBox().intersectAABB(getWorld().getGroundBB());
        if(intersectionWithGround.doesIntersects())
        {
            pos = pos.add(0, Math.abs(intersectionWithGround.getDistance()), 0);
            velocity.setY(0);
            onGround = true;
        }

        double angle = Math.acos(getRotation().getUp().dot(Vector3.yAxis));
        if(angle > Math.toRadians(89.99f))
        {
            if(getRotation().getForward().getY() > 0.f)
                rotate(getRotation().getRight(), -(float) (Math.toRadians(89.99f) - angle));
            else
                rotate(getRotation().getRight(), (float) (Math.toRadians(89.99f) - angle));
        }

        if(onGround)
        {
            velocity = velocity.mul(0.12f, 1, 0.12f);
        }
        else
            velocity = velocity.mul(0.12f / 2f, 1, 0.12f / 2f);
        lastTickPos = pos.copy();
    }

    /**
     * Checks if entity can go at given position
     */
    private boolean canGo(Vector3 pos)
    {
        AABB boundingBox = this.boundingBox.translate(pos);
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
                    Block block = getWorld().getBlock(x, y, z);
                    if(block == null)
                        continue;
                    AABB blockBB = block.getCollisionBox(getWorld(), x, y, z);
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
        pos.set(x, y, z);
    }

    public void moveBackwards(float distance)
    {
        moveForward(-distance);
    }

    public void moveForward(float distance)
    {
        velocity = velocity.add(rotation.getForward().setY(0).normalize().mul(distance));
    }

    public void moveLeft(float distance)
    {
        moveRight(-distance);
    }

    public void moveRight(float distance)
    {
        velocity = velocity.add(rotation.getRight().setY(0).normalize().mul(distance));
    }

    public void rotate(Vector3 axis, float radangle)
    {
        rotation = new Quaternion(axis, radangle).mul(rotation).normalize();
        headRotation = rotation.copy();
    }

    public void rotateHeadOnly(Vector3 axis, float radangle)
    {
        headRotation = new Quaternion(axis, radangle).mul(headRotation).normalize();
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

    public Quaternion getRotation()
    {
        return rotation;
    }

    public Quaternion getHeadRotation()
    {
        return headRotation;
    }

    public World getWorld()
    {
        return worldObj;
    }

    public Vector3 getPos()
    {
        return pos;
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
            velocity.setY(0.25f);
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
        getWorld().performRayCast(this, infos, maxDist);
        return infos;
    }

    /**
     * Returns distance in meters between this entity and given one
     */
    public float getDistance(Entity other)
    {
        return other.getPos().sub(getPos()).length();
    }
}
