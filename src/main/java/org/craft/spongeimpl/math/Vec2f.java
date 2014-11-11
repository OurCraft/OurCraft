package org.craft.spongeimpl.math;

import org.craft.maths.*;
import org.spongepowered.api.math.*;

public class Vec2f implements Vector2f
{

    /**
     * 
     */
    private static final long serialVersionUID = 3168977623016452608L;
    private float             x;
    private float             y;

    public Vec2f()
    {

    }

    public Vec2f(Vector2 v)
    {
        this.x = (float) v.getX();
        this.y = (float) v.getY();
    }

    public Vec2f(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2f(Vec2f v)
    {
        this.x = v.getX();
        this.y = v.getY();
    }

    @Override
    public float getX()
    {
        return x;
    }

    @Override
    public float getY()
    {
        return y;
    }

    @Override
    public Vector2f add(Vector2f v)
    {
        return new Vec2f(x + v.getX(), y + v.getY());
    }

    @Override
    public Vector2f add(double x, double y)
    {
        return new Vec2f(this.x + (float) x, this.y + (float) y);
    }

    @Override
    public Vector2f add(float x, float y)
    {
        return new Vec2f(this.x + (float) x, this.y + (float) y);
    }

    @Override
    public Vector2f sub(Vector2f v)
    {
        return new Vec2f(x - v.getX(), y - v.getY());
    }

    @Override
    public Vector2f sub(double x, double y)
    {
        return new Vec2f(this.x + (float) x, this.y + (float) y);
    }

    @Override
    public Vector2f sub(float x, float y)
    {
        return new Vec2f(this.x + (float) x, this.y + (float) y);
    }

    @Override
    public Vector2f mul(double a)
    {
        return new Vec2f(this.x * (float) a, this.y * (float) a);
    }

    @Override
    public Vector2f mul(float a)
    {
        return new Vec2f(this.x * (float) a, this.y * (float) a);
    }

    @Override
    public Vector2f mul(Vector2f v)
    {
        return new Vec2f(x * v.getX(), y * v.getY());
    }

    @Override
    public Vector2f mul(double x, double y)
    {
        return new Vec2f(this.x * (float) x, this.y * (float) y);
    }

    @Override
    public Vector2f mul(float x, float y)
    {
        return new Vec2f(this.x * (float) x, this.y * (float) y);
    }

    @Override
    public Vector2f div(double a)
    {
        return new Vec2f(this.x / (float) a, this.y / (float) a);
    }

    @Override
    public Vector2f div(float a)
    {
        return new Vec2f(this.x / (float) a, this.y / (float) a);
    }

    @Override
    public Vector2f div(Vector2f v)
    {
        return new Vec2f(x / v.getX(), y / v.getY());
    }

    @Override
    public Vector2f div(double x, double y)
    {
        return new Vec2f(this.x / (float) x, this.y / (float) y);
    }

    @Override
    public Vector2f div(float x, float y)
    {
        return new Vec2f(this.x / (float) x, this.y / (float) y);
    }

    @Override
    public float dot(Vector2f v)
    {
        return this.x * v.getX() + this.y * v.getY();
    }

    @Override
    public float dot(double x, double y)
    {
        return (float) (this.x * x + this.y * y);
    }

    @Override
    public float dot(float x, float y)
    {
        return this.x * x + this.y * y;
    }

    @Override
    public Vector2f pow(double pow)
    {
        return new Vec2f((float) Math.pow(this.x, pow), (float) Math.pow(this.y, pow));
    }

    @Override
    public Vector2f pow(float power)
    {
        return new Vec2f((float) Math.pow(this.x, power), (float) Math.pow(this.y, power));
    }

    @Override
    public Vector2f abs()
    {
        return new Vec2f(Math.abs(this.x), Math.abs(this.y));
    }

    @Override
    public Vector2f negate()
    {
        return new Vec2f(-x, -y);
    }

    @Override
    public Vector2f min(Vector2f v)
    {
        return new Vec2f(Math.min(this.x, v.getX()), Math.min(this.y, v.getY()));
    }

    @Override
    public Vector2f min(double x, double y)
    {
        return new Vec2f((float) Math.min(this.x, x), (float) Math.min(this.y, y));
    }

    @Override
    public Vector2f min(float x, float y)
    {
        return new Vec2f(Math.min(this.x, x), Math.min(this.y, y));
    }

    @Override
    public Vector2f max(Vector2f v)
    {
        return new Vec2f(Math.max(this.x, v.getX()), Math.max(this.y, v.getY()));
    }

    @Override
    public Vector2f max(double x, double y)
    {
        return new Vec2f((float) Math.max(this.x, x), (float) Math.max(this.y, y));
    }

    @Override
    public Vector2f max(float x, float y)
    {
        return new Vec2f(Math.max(this.x, x), Math.max(this.y, y));
    }

    @Override
    public float distanceSquared(Vector2f v)
    {
        return this.x * v.getX() + this.y * v.getY();
    }

    @Override
    public float distanceSquared(double x, double y)
    {
        return (float) (this.x * x + this.y * y);
    }

    @Override
    public float distanceSquared(float x, float y)
    {
        return this.x * x + this.y * y;
    }

    @Override
    public float distance(Vector2f v)
    {
        return (float) Math.sqrt(distanceSquared(v));
    }

    @Override
    public float distance(double x, double y)
    {
        return (float) Math.sqrt(distanceSquared(x, y));
    }

    @Override
    public float distance(float x, float y)
    {
        return (float) Math.sqrt(distanceSquared(x, y));
    }

    @Override
    public float lengthSquared()
    {
        return x * x + y * y;
    }

    @Override
    public float length()
    {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override
    public int getMinAxis()
    {
        return (int) Math.min(x, y);
    }

    @Override
    public int getMaxAxis()
    {
        return (int) Math.max(x, y);
    }

    @Override
    public Vector3f toVector3()
    {
        return new Vec3f(x, y, 0);
    }

    @Override
    public Vector3f toVector3(double z)
    {
        return new Vec3f(x, y, (float) z);
    }

    @Override
    public Vector3f toVector3(float z)
    {
        return new Vec3f(x, y, z);
    }

    @Override
    public float[] toArray()
    {
        return new float[]
        {
                x, y
        };
    }

    @Override
    public Vector2i toInt()
    {
        return new Vec2i((int) x, (int) y);
    }

    @Override
    public Vector2d toDouble()
    {
        return new Vec2d(x, y);
    }

    @Override
    public int compareTo(Vector2f v)
    {
        // TODO
        return 0;
    }

    @Override
    public Vector2f clone()
    {
        return new Vec2f(x, y);
    }

    @Override
    public int getFloorX()
    {
        return (int) Math.floor(x);
    }

    @Override
    public int getFloorY()
    {
        return (int) Math.floor(y);
    }

    @Override
    public Vector2f ceil()
    {
        return new Vec2f((float) Math.ceil(x), (float) Math.ceil(y));
    }

    @Override
    public Vector2f floor()
    {
        return new Vec2f((float) Math.floor(x), (float) Math.floor(y));
    }

    @Override
    public Vector2f round()
    {
        return new Vec2f((float) Math.round(x), (float) Math.round(y));
    }

    @Override
    public Vector2f normalize()
    {
        float l = length();
        float x1 = x / l;
        float y1 = y / l;
        return new Vec2f(x1, y1);
    }

    public Vector2f project(Vector2f b)
    {
        return b.mul(dot(b) / b.dot(b));
    }

    @Override
    public Vector2f project(float x, float y)
    {
        return project(new Vec2f(x, y));
    }

    @Override
    public Vector2f project(double x, double y)
    {
        return project(new Vec2f((float) x, (float) y));
    }

}
