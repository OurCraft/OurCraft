package org.craft.spongeimpl.math;

import org.craft.maths.*;
import org.spongepowered.api.math.*;

public class Vec2d implements Vector2d
{

    /**
     * 
     */
    private static final long serialVersionUID = 7030565296371095519L;
    private double            x;
    private double            y;

    public Vec2d()
    {

    }

    public Vec2d(Vector2 v)
    {
        this.x = (double) v.getX();
        this.y = (double) v.getY();
    }

    public Vec2d(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2d(Vec2d v)
    {
        this.x = v.getX();
        this.y = v.getY();
    }

    @Override
    public double getX()
    {
        return x;
    }

    @Override
    public double getY()
    {
        return y;
    }

    @Override
    public Vector2d add(Vector2d v)
    {
        return new Vec2d(x + v.getX(), y + v.getY());
    }

    @Override
    public Vector2d add(double x, double y)
    {
        return new Vec2d(this.x + (double) x, this.y + (double) y);
    }

    @Override
    public Vector2d sub(Vector2d v)
    {
        return new Vec2d(x - v.getX(), y - v.getY());
    }

    @Override
    public Vector2d sub(double x, double y)
    {
        return new Vec2d(this.x + (double) x, this.y + (double) y);
    }

    @Override
    public Vector2d mul(double a)
    {
        return new Vec2d(this.x * (double) a, this.y * (double) a);
    }

    @Override
    public Vector2d mul(Vector2d v)
    {
        return new Vec2d(x * v.getX(), y * v.getY());
    }

    @Override
    public Vector2d mul(double x, double y)
    {
        return new Vec2d(this.x * (double) x, this.y * (double) y);
    }

    @Override
    public Vector2d div(double a)
    {
        return new Vec2d(this.x / (double) a, this.y / (double) a);
    }

    @Override
    public Vector2d div(Vector2d v)
    {
        return new Vec2d(x / v.getX(), y / v.getY());
    }

    @Override
    public Vector2d div(double x, double y)
    {
        return new Vec2d(this.x / (double) x, this.y / (double) y);
    }

    @Override
    public double dot(Vector2d v)
    {
        return this.x * v.getX() + this.y * v.getY();
    }

    @Override
    public double dot(double x, double y)
    {
        return (double) (this.x * x + this.y * y);
    }

    @Override
    public Vector2d pow(double pow)
    {
        return new Vec2d((double) Math.pow(this.x, pow), (double) Math.pow(this.y, pow));
    }

    @Override
    public Vector2d abs()
    {
        return new Vec2d(Math.abs(this.x), Math.abs(this.y));
    }

    @Override
    public Vector2d negate()
    {
        return new Vec2d(-x, -y);
    }

    @Override
    public Vector2d min(Vector2d v)
    {
        return new Vec2d(Math.min(this.x, v.getX()), Math.min(this.y, v.getY()));
    }

    @Override
    public Vector2d min(double x, double y)
    {
        return new Vec2d((double) Math.min(this.x, x), (double) Math.min(this.y, y));
    }

    @Override
    public Vector2d max(Vector2d v)
    {
        return new Vec2d(Math.max(this.x, v.getX()), Math.max(this.y, v.getY()));
    }

    @Override
    public Vector2d max(double x, double y)
    {
        return new Vec2d((double) Math.max(this.x, x), (double) Math.max(this.y, y));
    }

    @Override
    public double distanceSquared(Vector2d v)
    {
        return this.x * v.getX() + this.y * v.getY();
    }

    @Override
    public double distanceSquared(double x, double y)
    {
        return (double) (this.x * x + this.y * y);
    }

    @Override
    public double distance(Vector2d v)
    {
        return (double) Math.sqrt(distanceSquared(v));
    }

    @Override
    public double distance(double x, double y)
    {
        return (double) Math.sqrt(distanceSquared(x, y));
    }

    @Override
    public double lengthSquared()
    {
        return x * x + y * y;
    }

    @Override
    public double length()
    {
        return (double) Math.sqrt(lengthSquared());
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
    public Vector3d toVector3()
    {
        return new Vec3d(x, y, 0);
    }

    @Override
    public Vector3d toVector3(double z)
    {
        return new Vec3d(x, y, (double) z);
    }

    @Override
    public double[] toArray()
    {
        return new double[]
        {
                x, y
        };
    }

    @Override
    public Vector2f toFloat()
    {
        return new Vec2f((float) x, (float) y);
    }

    @Override
    public Vector2i toInt()
    {
        return new Vec2i((int) x, (int) y);
    }

    @Override
    public int compareTo(Vector2d v)
    {
        // TODO
        return 0;
    }

    @Override
    public Vector2d clone()
    {
        return new Vec2d(x, y);
    }

    @Override
    public long getFloorX()
    {
        return (long) Math.floor(x);
    }

    @Override
    public long getFloorY()
    {
        return (long) Math.floor(y);
    }

    @Override
    public Vector2d ceil()
    {
        return new Vec2d(Math.ceil(x), Math.ceil(y));
    }

    @Override
    public Vector2d floor()
    {
        return new Vec2d(Math.floor(x), Math.floor(y));
    }

    @Override
    public Vector2d round()
    {
        return new Vec2d(Math.round(x), Math.round(y));
    }

    @Override
    public Vector2d normalize()
    {
        double l = length();
        double x1 = x / l;
        double y1 = y / l;
        return new Vec2d(x1, y1);
    }

}
