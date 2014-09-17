package org.craft.entity;

import org.craft.maths.*;
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

    public static final float G = 9.81f / 60f;

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

    public void setSize(float width, float height, float depth)
    {
        this.boundingBox = new AABB(Vector3.get(0, 0, 0), Vector3.get(width, height, depth));
    }

    public AABB getBoundingBox()
    {
        return new AABB(boundingBox.getMinExtents().add(pos), boundingBox.getMaxExtents().add(pos));
    }

    public void update()
    {
        onEntityUpdate();

        // TODO: gravity, movements'n'stuff
        velocity = velocity.add(0, -G, 0);

        if(velocity.getY() < -G * 8)
        {
            velocity.setY(-G * 8);
        }

        pos = pos.add(velocity);

        IntersectionInfos intersectionWithGround = getBoundingBox().intersectAABB(getWorld().getGroundBB());
        if(intersectionWithGround.doesIntersects())
        {
            pos = pos.add(0, Math.abs(intersectionWithGround.getDistance()), 0);
            velocity.setY(0);
        }

        double angle = Math.acos(getRotation().getUp().dot(Vector3.yAxis));
        if(angle > Math.toRadians(90))
        {
            if(getRotation().getForward().getY() > 0.f)
                rotate(getRotation().getRight(), -(float)(Math.toRadians(90) - angle));
            else
                rotate(getRotation().getRight(), (float)(Math.toRadians(90) - angle));
        }

        lastTickPos = pos.copy();
    }

    public void setLocation(float x, float y, float z)
    {
        pos.set(x, y + pos.getY(), z);
    }

    public void moveBackwards(float distance)
    {
        moveForward(-distance);
    }

    public void moveForward(float distance)
    {
        pos = pos.add(rotation.getForward().mul(distance));
    }

    public void moveLeft(float distance)
    {
        moveRight(-distance);
    }

    public void moveRight(float distance)
    {
        pos = pos.add(rotation.getRight().mul(distance));
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

    public void setDead()
    {
        this.isDead = false;
    }

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
}
