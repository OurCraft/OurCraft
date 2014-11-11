package org.craft.spongeimpl.math;

import org.craft.maths.*;
import org.spongepowered.api.math.*;

public class Vec2i implements Vector2i
{

    /**
     * 
     */
    private static final long serialVersionUID = -6663701267942355142L;
    private int               x;
    private int               y;

    public Vec2i()
    {

    }

    public Vec2i(Vector2 v)
    {
        this.x = (int) v.getX();
        this.y = (int) v.getY();
    }

    public Vec2i(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2i(Vec2i v)
    {
        this.x = v.getX();
        this.y = v.getY();
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    @Override
    public Vector2i add(Vector2i v)
    {
        return new Vec2i(x + v.getX(), y + v.getY());
    }

    @Override
    public Vector2i add(double x, double y)
    {
        return new Vec2i(this.x + (int) x, this.y + (int) y);
    }

    @Override
    public Vector2i add(int x, int y)
    {
        return new Vec2i(this.x + (int) x, this.y + (int) y);
    }

    @Override
    public Vector2i sub(Vector2i v)
    {
        return new Vec2i(x - v.getX(), y - v.getY());
    }

    @Override
    public Vector2i sub(double x, double y)
    {
        return new Vec2i(this.x + (int) x, this.y + (int) y);
    }

    @Override
    public Vector2i sub(int x, int y)
    {
        return new Vec2i(this.x + (int) x, this.y + (int) y);
    }

    @Override
    public Vector2i mul(double a)
    {
        return new Vec2i(this.x * (int) a, this.y * (int) a);
    }

    @Override
    public Vector2i mul(int a)
    {
        return new Vec2i(this.x * (int) a, this.y * (int) a);
    }

    @Override
    public Vector2i mul(Vector2i v)
    {
        return new Vec2i(x * v.getX(), y * v.getY());
    }

    @Override
    public Vector2i mul(double x, double y)
    {
        return new Vec2i(this.x * (int) x, this.y * (int) y);
    }

    @Override
    public Vector2i mul(int x, int y)
    {
        return new Vec2i(this.x * (int) x, this.y * (int) y);
    }

    @Override
    public Vector2i div(double a)
    {
        return new Vec2i(this.x / (int) a, this.y / (int) a);
    }

    @Override
    public Vector2i div(int a)
    {
        return new Vec2i(this.x / (int) a, this.y / (int) a);
    }

    @Override
    public Vector2i div(Vector2i v)
    {
        return new Vec2i(x / v.getX(), y / v.getY());
    }

    @Override
    public Vector2i div(double x, double y)
    {
        return new Vec2i(this.x / (int) x, this.y / (int) y);
    }

    @Override
    public Vector2i div(int x, int y)
    {
        return new Vec2i(this.x / (int) x, this.y / (int) y);
    }

    @Override
    public int dot(Vector2i v)
    {
        return this.x * v.getX() + this.y * v.getY();
    }

    @Override
    public int dot(double x, double y)
    {
        return (int) (this.x * x + this.y * y);
    }

    @Override
    public int dot(int x, int y)
    {
        return this.x * x + this.y * y;
    }

    @Override
    public Vector2i pow(double pow)
    {
        return new Vec2i((int) Math.pow(this.x, pow), (int) Math.pow(this.y, pow));
    }

    @Override
    public Vector2i pow(int power)
    {
        return new Vec2i((int) Math.pow(this.x, power), (int) Math.pow(this.y, power));
    }

    @Override
    public Vector2i abs()
    {
        return new Vec2i(Math.abs(this.x), Math.abs(this.y));
    }

    @Override
    public Vector2i negate()
    {
        return new Vec2i(-x, -y);
    }

    @Override
    public Vector2i min(Vector2i v)
    {
        return new Vec2i(Math.min(this.x, v.getX()), Math.min(this.y, v.getY()));
    }

    @Override
    public Vector2i min(double x, double y)
    {
        return new Vec2i((int) Math.min(this.x, x), (int) Math.min(this.y, y));
    }

    @Override
    public Vector2i min(int x, int y)
    {
        return new Vec2i(Math.min(this.x, x), Math.min(this.y, y));
    }

    @Override
    public Vector2i max(Vector2i v)
    {
        return new Vec2i(Math.max(this.x, v.getX()), Math.max(this.y, v.getY()));
    }

    @Override
    public Vector2i max(double x, double y)
    {
        return new Vec2i((int) Math.max(this.x, x), (int) Math.max(this.y, y));
    }

    @Override
    public Vector2i max(int x, int y)
    {
        return new Vec2i(Math.max(this.x, x), Math.max(this.y, y));
    }

    @Override
    public int distanceSquared(Vector2i v)
    {
        return this.x * v.getX() + this.y * v.getY();
    }

    @Override
    public int distanceSquared(double x, double y)
    {
        return (int) (this.x * x + this.y * y);
    }

    @Override
    public int distanceSquared(int x, int y)
    {
        return this.x * x + this.y * y;
    }

    @Override
    public int distance(Vector2i v)
    {
        return (int) Math.sqrt(distanceSquared(v));
    }

    @Override
    public int distance(double x, double y)
    {
        return (int) Math.sqrt(distanceSquared(x, y));
    }

    @Override
    public int distance(int x, int y)
    {
        return (int) Math.sqrt(distanceSquared(x, y));
    }

    @Override
    public int lengthSquared()
    {
        return x * x + y * y;
    }

    @Override
    public int length()
    {
        return (int) Math.sqrt(lengthSquared());
    }

    @Override
    public int getMinAxis()
    {
        return Math.min(x, y);
    }

    @Override
    public int getMaxAxis()
    {
        return Math.max(x, y);
    }

    @Override
    public Vector3i toVector3()
    {
        return new Vec3i(x, y, 0);
    }

    @Override
    public Vector3i toVector3(double z)
    {
        return new Vec3i(x, y, (int) z);
    }

    @Override
    public Vector3i toVector3(int z)
    {
        return new Vec3i(x, y, z);
    }

    @Override
    public int[] toArray()
    {
        return new int[]
        {
                x, y
        };
    }

    @Override
    public Vector2f toFloat()
    {
        return new Vec2f(x, y);
    }

    @Override
    public Vector2d toDouble()
    {
        return new Vec2d(x, y);
    }

    @Override
    public int compareTo(Vector2i v)
    {
        // TODO
        return 0;
    }

    @Override
    public Vector2i clone()
    {
        return new Vec2i(x, y);
    }

    @Override
    public Vector2i project(Vector2i b)
    {
        return b.mul(dot(b) / b.dot(b));
    }

    @Override
    public Vector2i project(int x, int y)
    {
        return project(new Vec2i(x, y));
    }

    @Override
    public Vector2i project(double x, double y)
    {
        return project(new Vec2i((int) x, (int) y));
    }
}
