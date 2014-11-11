package org.craft.spongeimpl.math;

import org.craft.maths.*;
import org.spongepowered.api.math.*;

public class Vec3i implements Vector3i
{

    /**
     * 
     */
    private static final long serialVersionUID = 4005959954267878505L;
    private int               x;
    private int               y;
    private int               z;

    public Vec3i()
    {

    }

    public Vec3i(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i(Vec3i v)
    {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    public Vec3i(Vector3 v)
    {
        this.x = (int) v.getX();
        this.y = (int) v.getY();
        this.z = (int) v.getZ();
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
    public int getZ()
    {
        return z;
    }

    @Override
    public Vector3i add(Vector3i v)
    {
        return new Vec3i(this.x + (int) v.getX(), this.y + (int) v.getY(), this.z + (int) v.getZ());
    }

    @Override
    public Vector3i add(double x, double y, double z)
    {
        return new Vec3i(this.x + (int) x, this.y + (int) y, this.z + (int) z);
    }

    @Override
    public Vector3i add(int x, int y, int z)
    {
        return new Vec3i(x + this.x, y + this.y, z + this.z);
    }

    @Override
    public Vector3i sub(Vector3i v)
    {
        return new Vec3i(this.x - (int) v.getX(), this.y - (int) v.getY(), this.z - (int) v.getZ());
    }

    @Override
    public Vector3i sub(double x, double y, double z)
    {
        return new Vec3i(this.x - (int) x, this.y - (int) y, this.z - (int) z);
    }

    @Override
    public Vector3i sub(int x, int y, int z)
    {
        return new Vec3i(x * this.x, y * this.y, z * this.z);
    }

    @Override
    public Vector3i mul(double a)
    {
        return new Vec3i((int) (x * a), (int) (y * a), (int) (z * a));
    }

    @Override
    public Vector3i mul(int a)
    {
        return new Vec3i((int) (x * a), (int) (y * a), (int) (z * a));
    }

    @Override
    public Vector3i mul(Vector3i v)
    {
        return new Vec3i((int) (x * v.getX()), (int) (y * v.getY()), (int) (z * v.getZ()));
    }

    @Override
    public Vector3i mul(double x, double y, double z)
    {
        return new Vec3i((int) (this.x * x), (int) (this.y * y), (int) (this.z * z));
    }

    @Override
    public Vector3i mul(int x, int y, int z)
    {
        return new Vec3i((int) (this.x * x), (int) (this.y * y), (int) (this.z * z));
    }

    @Override
    public Vector3i div(double a)
    {
        return new Vec3i((int) (x / a), (int) (y / a), (int) (z / a));
    }

    @Override
    public Vector3i div(int a)
    {
        return new Vec3i((int) (x / a), (int) (y / a), (int) (z / a));
    }

    @Override
    public Vector3i div(Vector3i v)
    {
        return new Vec3i((int) (x / v.getX()), (int) (y / v.getY()), (int) (z / v.getZ()));
    }

    @Override
    public Vector3i div(double x, double y, double z)
    {
        return new Vec3i((int) (this.x / x), (int) (this.y / y), (int) (this.z / z));
    }

    @Override
    public Vector3i div(int x, int y, int z)
    {
        return new Vec3i((int) (this.x / x), (int) (this.y / y), (int) (this.z / z));
    }

    @Override
    public int dot(Vector3i v)
    {
        return x * v.getX() + y * v.getY() + z * v.getY();
    }

    @Override
    public int dot(double x, double y, double z)
    {
        return (int) (this.x * x + this.y * y + this.z * z);
    }

    @Override
    public int dot(int x, int y, int z)
    {
        return (int) (this.x * x + this.y * y + this.z * z);
    }

    @Override
    public Vector3i cross(Vector3i v)
    {
        int x = this.y * v.getZ() - this.z * v.getY();
        int y = this.z * v.getX() - this.x * v.getZ();
        int z = this.x * v.getY() - this.y * v.getX();
        return new Vec3i(x, y, z);
    }

    @Override
    public Vector3i cross(double x, double y, double z)
    {
        int x1 = (int) (this.y * z - this.z * y);
        int y1 = (int) (this.z * x - this.x * z);
        int z1 = (int) (this.x * y - this.y * x);
        return new Vec3i(x1, y1, z1);
    }

    @Override
    public Vector3i cross(int x, int y, int z)
    {
        int x1 = (int) (this.y * z - this.z * y);
        int y1 = (int) (this.z * x - this.x * z);
        int z1 = (int) (this.x * y - this.y * x);
        return new Vec3i(x1, y1, z1);
    }

    @Override
    public Vector3i pow(double pow)
    {
        return new Vec3i((int) Math.pow(x, pow), (int) Math.pow(y, pow), (int) Math.pow(z, pow));
    }

    @Override
    public Vector3i pow(int pow)
    {
        return new Vec3i((int) Math.pow(x, pow), (int) Math.pow(y, pow), (int) Math.pow(z, pow));
    }

    @Override
    public Vector3i abs()
    {
        return new Vec3i(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    @Override
    public Vector3i negate()
    {
        return new Vec3i(-x, -y, -z);
    }

    @Override
    public Vector3i min(Vector3i v)
    {
        return new Vec3i(Math.min(x, v.getX()), Math.min(y, v.getY()), Math.min(z, v.getZ()));
    }

    @Override
    public Vector3i min(double x, double y, double z)
    {
        return new Vec3i((int) Math.min(this.x, x), (int) Math.min(this.y, y), (int) Math.min(this.z, z));
    }

    @Override
    public Vector3i min(int x, int y, int z)
    {
        return new Vec3i((int) Math.min(this.x, x), (int) Math.min(this.y, y), (int) Math.min(this.z, z));
    }

    @Override
    public Vector3i max(Vector3i v)
    {
        return new Vec3i(Math.max(x, v.getX()), Math.max(y, v.getY()), Math.max(z, v.getZ()));
    }

    @Override
    public Vector3i max(double x, double y, double z)
    {
        return new Vec3i((int) Math.max(this.x, x), (int) Math.max(this.y, y), (int) Math.max(this.z, z));
    }

    @Override
    public Vector3i max(int x, int y, int z)
    {
        return new Vec3i((int) Math.max(this.x, x), (int) Math.max(this.y, y), (int) Math.max(this.z, z));
    }

    @Override
    public double distanceSquared(Vector3i v)
    {
        int dx = this.x - v.getX();
        int dy = this.y - v.getY();
        int dz = this.z - v.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public double distanceSquared(double x, double y, double z)
    {
        int dx = (int) (this.x - x);
        int dy = (int) (this.y - y);
        int dz = (int) (this.z - z);
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public double distanceSquared(int x, int y, int z)
    {
        int dx = this.x - x;
        int dy = this.y - y;
        int dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public double distance(Vector3i v)
    {
        int dx = this.x - v.getX();
        int dy = this.y - v.getY();
        int dz = this.z - v.getZ();
        return (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public double distance(double x, double y, double z)
    {
        int dx = (int) (this.x - x);
        int dy = (int) (this.y - y);
        int dz = (int) (this.z - z);
        return (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public double distance(int x, int y, int z)
    {
        int dx = this.x - x;
        int dy = this.y - y;
        int dz = this.z - z;
        return (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public double lengthSquared()
    {
        return x * x + y * y + z * z;
    }

    @Override
    public double length()
    {
        return (int) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public int getMinAxis()
    {
        return Math.min(x, Math.min(y, z));
    }

    @Override
    public int getMaxAxis()
    {
        return Math.max(x, Math.max(y, z));
    }

    @Override
    public Vector2i toVector2()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector2i toVector2(boolean useZ)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] toArray()
    {
        return new int[]
        {
                x, y, z
        };
    }

    @Override
    public Vector3f toFloat()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector3d toDouble()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int compareTo(Vector3i v)
    {
        // TODO
        return 0;
    }

    @Override
    public Vector3i clone()
    {
        return new Vec3i(x, y, z);
    }

    @Override
    public Vector3i project(Vector3i b)
    {
        return b.mul(dot(b) / b.dot(b));
    }

    @Override
    public Vector3i project(double x, double y, double z)
    {
        return project(new Vec3i((int) x, (int) y, (int) z));
    }

    @Override
    public Vector3i project(int x, int y, int z)
    {
        return project(new Vec3i(x, y, z));
    }

}
