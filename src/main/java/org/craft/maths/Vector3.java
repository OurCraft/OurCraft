package org.craft.maths;

import java.nio.*;

import org.craft.utils.*;

public class Vector3 extends AbstractReference implements IDisposable, IBufferWritable
{

    public static final Vector3 NULL  = new Vector3(0, 0, 0);

    public static final Vector3 yAxis = new Vector3(0, 1, 0);

    public static final Vector3 xAxis = new Vector3(1, 0, 0);

    public static final Vector3 zAxis = new Vector3(0, 0, 1);

    private float               x;
    private float               y;
    private float               z;

    private Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private Vector3()
    {
        ;
    }

    public String toString()
    {
        return "vec3(" + x + "," + y + "," + z + ")";
    }

    public Vector3 normalize()
    {
        float l = length();
        if(l == 0)
            return Vector3.NULL;
        float _x = this.x / l;
        float _y = this.y / l;
        float _z = this.z / l;
        return Vector3.get(_x, _y, _z);
    }

    /**
     * @param v
     */
    public float dot(Vector3 r)
    {
        return x * r.x + y * r.y + z * r.z;
    }

    public Vector3 cross(Vector3 v)
    {
        float x = this.y * v.z - this.z * v.y;
        float y = this.z * v.x - this.x * v.z;
        float z = this.x * v.y - this.y * v.x;
        return Vector3.get(x, y, z);
    }

    public Vector3 add(float factor)
    {
        return Vector3.get(this.x + factor, this.y + factor, this.z + factor);
    }

    public Vector3 add(float x, float y, float z)
    {
        return Vector3.get(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 add(Vector3 v)
    {
        return Vector3.get(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public Vector3 sub(float factor)
    {
        return Vector3.get(this.x - factor, this.y - factor, this.z - factor);
    }

    public Vector3 sub(float x, float y, float z)
    {
        return Vector3.get(this.x - x, this.y - y, this.z - z);
    }

    public Vector3 sub(Vector3 v)
    {
        return Vector3.get(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public Vector3 div(float factor)
    {
        return Vector3.get(this.x / factor, this.y / factor, this.z / factor);
    }

    public Vector3 div(float x, float y, float z)
    {
        return Vector3.get(this.x / x, this.y / y, this.z / z);
    }

    public Vector3 div(Vector3 v)
    {
        return Vector3.get(this.x / v.x, this.y / v.y, this.z / v.z);
    }

    public Vector3 mul(float factor)
    {
        return Vector3.get(this.x * factor, this.y * factor, this.z * factor);
    }

    public Vector3 mul(Vector3 v)
    {
        return Vector3.get(this.x * v.x, this.y * v.y, this.z * v.z);
    }

    public Vector3 mul(float x, float y, float z)
    {
        return Vector3.get(this.x * x, this.y * y, this.z * z);
    }

    public float length()
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public void write(FloatBuffer buffer)
    {
        buffer.put((float) getX());
        buffer.put((float) getY());
        buffer.put((float) getZ());
    }

    public int getSize()
    {
        return 3;
    }

    public Vector3 rotate(float angle, Vector3 axis)
    {
        float sinAngle = (float) Math.sin(-angle);
        float cosAngle = (float) Math.cos(-angle);
        return this.cross(axis.mul(sinAngle)).add( // Rotation on local X
        (this.mul(cosAngle)).add( // Rotation on local Z
        axis.mul(this.dot(axis.mul(1 - cosAngle))))); // Rotation
                                                      // on
                                                      // local Y
    }

    public Vector3 rotate(Quaternion rotation)
    {
        Quaternion conjugate = rotation.conjugate();

        Quaternion w = rotation.mul(copy()).mul(conjugate);

        return Vector3.get(w.getX(), w.getY(), w.getZ());
    }

    public Vector3 copy()
    {
        return Vector3.get(x, y, z);
    }

    public Vector3 negative()
    {
        return Vector3.get(-x, -y, -z);
    }

    public Vector2 xy()
    {
        return Vector2.get(x, y);
    }

    public Vector2 xz()
    {
        return Vector2.get(x, z);
    }

    public Vector2 zx()
    {
        return Vector2.get(z, x);
    }

    public Vector2 yx()
    {
        return Vector2.get(y, x);
    }

    public Vector2 zy()
    {
        return Vector2.get(z, y);
    }

    public Vector2 yz()
    {
        return Vector2.get(y, z);
    }

    public Vector3 lerp(Vector3 dest, float factor)
    {
        return dest.sub(this).mul(factor).add(this);
    }

    public float max()
    {
        return (float) Math.max(getX(), Math.max(getY(), getZ()));
    }

    public Vector3 set(Vector3 v)
    {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
        return this;
    }

    public Vector3 setX(float x)
    {
        this.x = x;
        return this;
    }

    public Vector3 setY(float y)
    {
        this.y = y;
        return this;
    }

    public Vector3 setZ(float z)
    {
        this.z = z;
        return this;
    }

    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        if(o instanceof Vector3)
        {
            Vector3 v = (Vector3) o;
            return v.x == x && v.y == y && v.z == z;
        }
        if(o instanceof float[])
        {
            float[] array = (float[]) o;
            return array[0] == x && array[1] == y && array[2] == z;
        }
        return false;
    }

    public int hashCode()
    {
        final int BASE = 17;
        final int MULTIPLIER = 31;

        int result = BASE;
        result = MULTIPLIER * result + Float.floatToIntBits(x);
        result = MULTIPLIER * result + Float.floatToIntBits(y);
        result = MULTIPLIER * result + Float.floatToIntBits(z);
        return result;
    }

    public static Vector3 cross(Vector3 a, Vector3 b)
    {
        return a.cross(b);
    }

    public static float dot(Vector3 a, Vector3 b)
    {
        return a.dot(b);
    }

    public static Vector3 max(Vector3 a, Vector3 b)
    {
        return Vector3.get((float) Math.max(a.x, b.x), (float) Math.max(a.y, b.y), (float) Math.max(a.z, b.z));
    }

    private static ObjectPool<Vector3> pool = ObjectPool.of(Vector3.class);

    public static Vector3 get(float x, float y, float z)
    {
        Vector3 v = pool.get();
        v.increaseReferenceCounter();
        v.set(x, y, z);
        return v;
    }

    public float min()
    {
        return (float) Math.min(getX(), Math.min(getY(), getZ()));
    }

    @Override
    public void dispose()
    {
        if(decreaseReferenceCounter())
        {
            pool.dispose(this);
        }
    }

    public boolean isNull()
    {
        return x == 0 && y == 0 && z == 0;
    }

}
