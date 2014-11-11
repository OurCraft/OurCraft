package org.craft.spongeimpl.math;

import org.craft.maths.*;
import org.spongepowered.api.math.*;

public class Vec3f implements Vector3f
{

    /**
     * 
     */
    private static final long serialVersionUID = -1631772960470753205L;
    private float             x;
    private float             y;
    private float             z;

    public Vec3f()
    {

    }

    public Vec3f(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f(Vec3f v)
    {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    public Vec3f(Vector3 v)
    {
        this.x = (float) v.getX();
        this.y = (float) v.getY();
        this.z = (float) v.getZ();
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
    public float getZ()
    {
        return z;
    }

    @Override
    public Vector3f add(Vector3f v)
    {
        return new Vec3f(this.x + (float) v.getX(), this.y + (float) v.getY(), this.z + (float) v.getZ());
    }

    @Override
    public Vector3f add(double x, double y, double z)
    {
        return new Vec3f(this.x + (float) x, this.y + (float) y, this.z + (float) z);
    }

    @Override
    public Vector3f add(float x, float y, float z)
    {
        return new Vec3f(x + this.x, y + this.y, z + this.z);
    }

    @Override
    public Vector3f sub(Vector3f v)
    {
        return new Vec3f(this.x - (float) v.getX(), this.y - (float) v.getY(), this.z - (float) v.getZ());
    }

    @Override
    public Vector3f sub(double x, double y, double z)
    {
        return new Vec3f(this.x - (float) x, this.y - (float) y, this.z - (float) z);
    }

    @Override
    public Vector3f sub(float x, float y, float z)
    {
        return new Vec3f(x * this.x, y * this.y, z * this.z);
    }

    @Override
    public Vector3f mul(double a)
    {
        return new Vec3f((float) (x * a), (float) (y * a), (float) (z * a));
    }

    @Override
    public Vector3f mul(float a)
    {
        return new Vec3f((float) (x * a), (float) (y * a), (float) (z * a));
    }

    @Override
    public Vector3f mul(Vector3f v)
    {
        return new Vec3f((float) (x * v.getX()), (float) (y * v.getY()), (float) (z * v.getZ()));
    }

    @Override
    public Vector3f mul(double x, double y, double z)
    {
        return new Vec3f((float) (this.x * x), (float) (this.y * y), (float) (this.z * z));
    }

    @Override
    public Vector3f mul(float x, float y, float z)
    {
        return new Vec3f((float) (this.x * x), (float) (this.y * y), (float) (this.z * z));
    }

    @Override
    public Vector3f div(double a)
    {
        return new Vec3f((float) (x / a), (float) (y / a), (float) (z / a));
    }

    @Override
    public Vector3f div(float a)
    {
        return new Vec3f((float) (x / a), (float) (y / a), (float) (z / a));
    }

    @Override
    public Vector3f div(Vector3f v)
    {
        return new Vec3f((float) (x / v.getX()), (float) (y / v.getY()), (float) (z / v.getZ()));
    }

    @Override
    public Vector3f div(double x, double y, double z)
    {
        return new Vec3f((float) (this.x / x), (float) (this.y / y), (float) (this.z / z));
    }

    @Override
    public Vector3f div(float x, float y, float z)
    {
        return new Vec3f((float) (this.x / x), (float) (this.y / y), (float) (this.z / z));
    }

    @Override
    public float dot(Vector3f v)
    {
        return x * v.getX() + y * v.getY() + z * v.getY();
    }

    @Override
    public float dot(double x, double y, double z)
    {
        return (float) (this.x * x + this.y * y + this.z * z);
    }

    @Override
    public float dot(float x, float y, float z)
    {
        return (float) (this.x * x + this.y * y + this.z * z);
    }

    @Override
    public Vector3f cross(Vector3f v)
    {
        float x = this.y * v.getZ() - this.z * v.getY();
        float y = this.z * v.getX() - this.x * v.getZ();
        float z = this.x * v.getY() - this.y * v.getX();
        return new Vec3f(x, y, z);
    }

    @Override
    public Vector3f cross(double x, double y, double z)
    {
        float x1 = (float) (this.y * z - this.z * y);
        float y1 = (float) (this.z * x - this.x * z);
        float z1 = (float) (this.x * y - this.y * x);
        return new Vec3f(x1, y1, z1);
    }

    @Override
    public Vector3f cross(float x, float y, float z)
    {
        float x1 = (float) (this.y * z - this.z * y);
        float y1 = (float) (this.z * x - this.x * z);
        float z1 = (float) (this.x * y - this.y * x);
        return new Vec3f(x1, y1, z1);
    }

    @Override
    public Vector3f pow(double pow)
    {
        return new Vec3f((float) Math.pow(x, pow), (float) Math.pow(y, pow), (float) Math.pow(z, pow));
    }

    @Override
    public Vector3f pow(float pow)
    {
        return new Vec3f((float) Math.pow(x, pow), (float) Math.pow(y, pow), (float) Math.pow(z, pow));
    }

    @Override
    public Vector3f abs()
    {
        return new Vec3f(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    @Override
    public Vector3f negate()
    {
        return new Vec3f(-x, -y, -z);
    }

    @Override
    public Vector3f min(Vector3f v)
    {
        return new Vec3f(Math.min(x, v.getX()), Math.min(y, v.getY()), Math.min(z, v.getZ()));
    }

    @Override
    public Vector3f min(double x, double y, double z)
    {
        return new Vec3f((float) Math.min(this.x, x), (float) Math.min(this.y, y), (float) Math.min(this.z, z));
    }

    @Override
    public Vector3f min(float x, float y, float z)
    {
        return new Vec3f((float) Math.min(this.x, x), (float) Math.min(this.y, y), (float) Math.min(this.z, z));
    }

    @Override
    public Vector3f max(Vector3f v)
    {
        return new Vec3f(Math.max(x, v.getX()), Math.max(y, v.getY()), Math.max(z, v.getZ()));
    }

    @Override
    public Vector3f max(double x, double y, double z)
    {
        return new Vec3f((float) Math.max(this.x, x), (float) Math.max(this.y, y), (float) Math.max(this.z, z));
    }

    @Override
    public Vector3f max(float x, float y, float z)
    {
        return new Vec3f((float) Math.max(this.x, x), (float) Math.max(this.y, y), (float) Math.max(this.z, z));
    }

    @Override
    public float distanceSquared(Vector3f v)
    {
        float dx = this.x - v.getX();
        float dy = this.y - v.getY();
        float dz = this.z - v.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public float distanceSquared(double x, double y, double z)
    {
        float dx = (float) (this.x - x);
        float dy = (float) (this.y - y);
        float dz = (float) (this.z - z);
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public float distanceSquared(float x, float y, float z)
    {
        float dx = this.x - x;
        float dy = this.y - y;
        float dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public float distance(Vector3f v)
    {
        float dx = this.x - v.getX();
        float dy = this.y - v.getY();
        float dz = this.z - v.getZ();
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public float distance(double x, double y, double z)
    {
        float dx = (float) (this.x - x);
        float dy = (float) (this.y - y);
        float dz = (float) (this.z - z);
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public float distance(float x, float y, float z)
    {
        float dx = this.x - x;
        float dy = this.y - y;
        float dz = this.z - z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public float lengthSquared()
    {
        return x * x + y * y + z * z;
    }

    @Override
    public float length()
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
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
    public Vector2f toVector2()
    {
        return new Vec2f(x, y);
    }

    @Override
    public Vector2f toVector2(boolean useZ)
    {
        return new Vec2f(x, useZ ? z : y);
    }

    @Override
    public float[] toArray()
    {
        return new float[]
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
    public Vector3d toDouble()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int compareTo(Vector3f v)
    {
        // TODO
        return 0;
    }

    @Override
    public Vector3f clone()
    {
        return new Vec3f(x, y, z);
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
    public int getFloorZ()
    {
        return (int) Math.floor(z);
    }

    @Override
    public Vector3f ceil()
    {
        return new Vec3f((float) Math.ceil(x), (float) Math.ceil(y), (float) Math.ceil(z));
    }

    @Override
    public Vector3f floor()
    {
        return new Vec3f((float) Math.floor(x), (float) Math.floor(y), (float) Math.floor(z));
    }

    @Override
    public Vector3f round()
    {
        return new Vec3f((float) Math.round(x), (float) Math.round(y), (float) Math.round(z));
    }

    @Override
    public Vector3f normalize()
    {
        float l = length();
        float x1 = this.x /= l;
        float y1 = this.y /= l;
        float z1 = this.z /= l;
        return new Vec3f(x1, y1, z1);
    }

    @Override
    public Vector3f project(Vector3f b)
    {
        return b.mul(dot(b) / b.dot(b));
    }

    @Override
    public Vector3f project(float x, float y, float z)
    {
        return project(new Vec3f(x, y, z));
    }

    @Override
    public Vector3f project(double x, double y, double z)
    {
        return project(new Vec3f((float) x, (float) y, (float) z));
    }

}
