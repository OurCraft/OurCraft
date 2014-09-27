package org.craft.spongeimpl.math;

import org.craft.maths.*;
import org.spongepowered.api.math.*;

public class Vec3d implements Vector3d
{

    /**
     * 
     */
    private static final long serialVersionUID = -7396838635703596604L;
    private double            x;
    private double            y;
    private double            z;

    public Vec3d()
    {

    }

    public Vec3d(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d(Vec3d v)
    {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    public Vec3d(Vector3 v)
    {
        this.x = (double) v.getX();
        this.y = (double) v.getY();
        this.z = (double) v.getZ();
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
    public double getZ()
    {
        return z;
    }

    @Override
    public Vector3d add(Vector3d v)
    {
        return new Vec3d(this.x + (double) v.getX(), this.y + (double) v.getY(), this.z + (double) v.getZ());
    }

    @Override
    public Vector3d add(double x, double y, double z)
    {
        return new Vec3d(this.x + (double) x, this.y + (double) y, this.z + (double) z);
    }

    @Override
    public Vector3d sub(Vector3d v)
    {
        return new Vec3d(this.x - (double) v.getX(), this.y - (double) v.getY(), this.z - (double) v.getZ());
    }

    @Override
    public Vector3d sub(double x, double y, double z)
    {
        return new Vec3d(this.x - (double) x, this.y - (double) y, this.z - (double) z);
    }

    @Override
    public Vector3d mul(double a)
    {
        return new Vec3d((double) (x * a), (double) (y * a), (double) (z * a));
    }

    @Override
    public Vector3d mul(Vector3d v)
    {
        return new Vec3d((double) (x * v.getX()), (double) (y * v.getY()), (double) (z * v.getZ()));
    }

    @Override
    public Vector3d mul(double x, double y, double z)
    {
        return new Vec3d((double) (this.x * x), (double) (this.y * y), (double) (this.z * z));
    }

    @Override
    public Vector3d div(double a)
    {
        return new Vec3d((double) (x / a), (double) (y / a), (double) (z / a));
    }

    @Override
    public Vector3d div(Vector3d v)
    {
        return new Vec3d((double) (x / v.getX()), (double) (y / v.getY()), (double) (z / v.getZ()));
    }

    @Override
    public Vector3d div(double x, double y, double z)
    {
        return new Vec3d((double) (this.x / x), (double) (this.y / y), (double) (this.z / z));
    }

    @Override
    public double dot(Vector3d v)
    {
        return x * v.getX() + y * v.getY() + z * v.getY();
    }

    @Override
    public double dot(double x, double y, double z)
    {
        return (double) (this.x * x + this.y * y + this.z * z);
    }

    @Override
    public Vector3d cross(Vector3d v)
    {
        double x = this.y * v.getZ() - this.z * v.getY();
        double y = this.z * v.getX() - this.x * v.getZ();
        double z = this.x * v.getY() - this.y * v.getX();
        return new Vec3d(x, y, z);
    }

    @Override
    public Vector3d cross(double x, double y, double z)
    {
        double x1 = (double) (this.y * z - this.z * y);
        double y1 = (double) (this.z * x - this.x * z);
        double z1 = (double) (this.x * y - this.y * x);
        return new Vec3d(x1, y1, z1);
    }

    @Override
    public Vector3d pow(double pow)
    {
        return new Vec3d((double) Math.pow(x, pow), (double) Math.pow(y, pow), (double) Math.pow(z, pow));
    }

    @Override
    public Vector3d abs()
    {
        return new Vec3d(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    @Override
    public Vector3d negate()
    {
        return new Vec3d(-x, -y, -z);
    }

    @Override
    public Vector3d min(Vector3d v)
    {
        return new Vec3d(Math.min(x, v.getX()), Math.min(y, v.getY()), Math.min(z, v.getZ()));
    }

    @Override
    public Vector3d min(double x, double y, double z)
    {
        return new Vec3d((double) Math.min(this.x, x), (double) Math.min(this.y, y), (double) Math.min(this.z, z));
    }

    @Override
    public Vector3d max(Vector3d v)
    {
        return new Vec3d(Math.max(x, v.getX()), Math.max(y, v.getY()), Math.max(z, v.getZ()));
    }

    @Override
    public Vector3d max(double x, double y, double z)
    {
        return new Vec3d((double) Math.max(this.x, x), (double) Math.max(this.y, y), (double) Math.max(this.z, z));
    }

    @Override
    public double distanceSquared(Vector3d v)
    {
        double dx = this.x - v.getX();
        double dy = this.y - v.getY();
        double dz = this.z - v.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public double distanceSquared(double x, double y, double z)
    {
        double dx = (double) (this.x - x);
        double dy = (double) (this.y - y);
        double dz = (double) (this.z - z);
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public double distance(Vector3d v)
    {
        double dx = this.x - v.getX();
        double dy = this.y - v.getY();
        double dz = this.z - v.getZ();
        return (double) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public double distance(double x, double y, double z)
    {
        double dx = (double) (this.x - x);
        double dy = (double) (this.y - y);
        double dz = (double) (this.z - z);
        return (double) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public double lengthSquared()
    {
        return x * x + y * y + z * z;
    }

    @Override
    public double length()
    {
        return (double) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public int getMinAxis()
    {
        return (int) Math.min(x, Math.min(y, z));
    }

    @Override
    public int getMaxAxis()
    {
        return (int) Math.max(x, Math.max(y, z));
    }

    @Override
    public Vector2d toVector2()
    {
        return new Vec2d(x, y);
    }

    @Override
    public Vector2d toVector2(boolean useZ)
    {
        return new Vec2d(x, useZ ? z : y);
    }

    @Override
    public double[] toArray()
    {
        return new double[]
        {
                x, y, z
        };
    }

    @Override
    public Vector3i toInt()
    {
        return new Vec3i((int) x, (int) y, (int) z);
    }

    @Override
    public Vector3f toFloat()
    {
        return new Vec3f((float) x, (float) y, (float) z);
    }

    @Override
    public int compareTo(Vector3d v)
    {
        // TODO
        return 0;
    }

    @Override
    public Vector3d clone()
    {
        return new Vec3d(x, y, z);
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
    public long getFloorZ()
    {
        return (long) Math.floor(z);
    }

    @Override
    public Vector3d ceil()
    {
        return new Vec3d((double) Math.ceil(x), (double) Math.ceil(y), (double) Math.ceil(z));
    }

    @Override
    public Vector3d floor()
    {
        return new Vec3d((double) Math.floor(x), (double) Math.floor(y), (double) Math.floor(z));
    }

    @Override
    public Vector3d round()
    {
        return new Vec3d((double) Math.round(x), (double) Math.round(y), (double) Math.round(z));
    }

    @Override
    public Vector3d normalize()
    {
        double l = length();
        double x1 = this.x /= l;
        double y1 = this.y /= l;
        double z1 = this.z /= l;
        return new Vec3d(x1, y1, z1);
    }

}
