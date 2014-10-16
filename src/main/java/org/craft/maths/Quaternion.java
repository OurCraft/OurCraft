package org.craft.maths;

import java.nio.*;

public class Quaternion
{
    public static final Quaternion NULL = new Quaternion(0, 0, 0, 1);

    private float                  x;
    private float                  y;
    private float                  z;
    private float                  w;

    public Quaternion()
    {
        this(0, 0, 0, 1);
    }

    public Quaternion(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(Vector3 axis, float angle)
    {
        init(axis, angle);
    }

    // From Ken Shoemake's "Quaternion Calculus and Fast Animation" article
    public Quaternion(Matrix4 rot)
    {
        float trace = (float) (rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2));

        if(trace > 0)
        {
            float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
            w = 0.25f / s;
            x = (rot.get(1, 2) - rot.get(2, 1)) * s;
            y = (rot.get(2, 0) - rot.get(0, 2)) * s;
            z = (rot.get(0, 1) - rot.get(1, 0)) * s;
        }
        else
        {
            if(rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2))
            {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
                w = (rot.get(1, 2) - rot.get(2, 1)) / s;
                x = 0.25f * s;
                y = (rot.get(1, 0) + rot.get(0, 1)) / s;
                z = (rot.get(2, 0) + rot.get(0, 2)) / s;
            }
            else if(rot.get(1, 1) > rot.get(2, 2))
            {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
                w = (rot.get(2, 0) - rot.get(0, 2)) / s;
                x = (rot.get(1, 0) + rot.get(0, 1)) / s;
                y = 0.25f * s;
                z = (rot.get(2, 1) + rot.get(1, 2)) / s;
            }
            else
            {
                float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
                w = (rot.get(0, 1) - rot.get(1, 0)) / s;
                x = (rot.get(2, 0) + rot.get(0, 2)) / s;
                y = (rot.get(1, 2) + rot.get(2, 1)) / s;
                z = 0.25f * s;
            }
        }

        float length = length();
        x /= length;
        y /= length;
        z /= length;
        w /= length;
    }

    public Matrix4 toRotationMatrix()
    {
        Vector3 forward = Vector3.get(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
        Vector3 up = Vector3.get(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
        Vector3 right = Vector3.get(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

        return Matrix4.get().initRotation(forward, up, right);
    }

    public Vector3 getForward()
    {
        return Vector3.get(0, 0, 1).rotate(this);
    }

    public Vector3 getBack()
    {
        return Vector3.get(0, 0, -1).rotate(this);
    }

    public Vector3 getUp()
    {
        return Vector3.get(0, 1, 0).rotate(this);
    }

    public Vector3 getDown()
    {
        return Vector3.get(0, -1, 0).rotate(this);
    }

    public Vector3 getRight()
    {
        return Vector3.get(1, 0, 0).rotate(this);
    }

    public Vector3 getLeft()
    {
        return Vector3.get(-1, 0, 0).rotate(this);
    }

    public float length()
    {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest)
    {
        Quaternion correctedDest = dest;

        if(shortest && this.dot(dest) < 0)
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());

        return correctedDest.sub(this).mul(lerpFactor).add(this).normalize();
    }

    public Quaternion slerp(Quaternion dest, float lerpFactor, boolean shortest)
    {
        final float EPSILON = 1e3f;

        float cos = this.dot(dest);
        Quaternion correctedDest = dest;

        if(shortest && cos < 0)
        {
            cos = -cos;
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
        }

        if(Math.abs(cos) >= 1 - EPSILON)
            return nlerp(correctedDest, lerpFactor, false);

        float sin = (float) Math.sqrt(1.0f - cos * cos);
        float angle = (float) Math.atan2(sin, cos);
        float invSin = 1.0f / sin;

        float srcFactor = (float) (Math.sin((1.0f - lerpFactor) * angle) * invSin);
        float destFactor = (float) (Math.sin((lerpFactor) * angle) * invSin);

        return this.mul(srcFactor).add(correctedDest.mul(destFactor)).normalize();
    }

    public Quaternion sub(Quaternion r)
    {
        return new Quaternion(x - r.getX(), y - r.getY(), z - r.getZ(), w - r.getW());
    }

    public Quaternion add(Quaternion r)
    {
        return new Quaternion(x + r.getX(), y + r.getY(), z + r.getZ(), w + r.getW());
    }

    public float dot(Quaternion r)
    {
        return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
    }

    public Quaternion normalize()
    {
        float l = length();
        x /= l;
        y /= l;
        z /= l;
        w /= l;
        return this;
    }

    public Quaternion conjugate()
    {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion mul(float d)
    {
        return new Quaternion(x * d, y * d, z * d, w * d);
    }

    public Quaternion mul(Quaternion r)
    {
        float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
        float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
        float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
        float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();

        return new Quaternion(x_, y_, z_, w_);
    }

    public Quaternion mul(Vector3 r)
    {
        float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
        float x_ = w * r.getX() + y * r.getZ() - z * r.getY();
        float y_ = w * r.getY() + z * r.getX() - x * r.getZ();
        float z_ = w * r.getZ() + x * r.getY() - y * r.getX();
        return new Quaternion(x_, y_, z_, w_);
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getZ()
    {
        return z;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    public float getW()
    {
        return w;
    }

    public void setW(float w)
    {
        this.w = w;
    }

    public boolean equals(Object o)
    {
        if(o instanceof Quaternion)
        {
            Quaternion other = (Quaternion) o;
            return other.getX() == getX() && other.getY() == getY() && other.getZ() == getZ() && other.getW() == getW();
        }
        return false;
    }

    public Quaternion set(Quaternion v)
    {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
        this.w = v.getW();
        return this;
    }

    public Quaternion copy()
    {
        return new Quaternion(x, y, z, w);
    }

    public void write(FloatBuffer buffer)
    {
        buffer.put((float) getX());
        buffer.put((float) getY());
        buffer.put((float) getZ());
        buffer.put((float) getW());
    }

    public int getSize()
    {
        return 4;
    }

    public void set(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector3 xyz()
    {
        return Vector3.get(x, y, z);
    }

    public Quaternion div(Quaternion other)
    {
        return new Quaternion(x / other.x, y / other.y, z / other.z, w / other.w);
    }

    public Quaternion div(float factor)
    {
        return new Quaternion(x / factor, y / factor, z / factor, w / factor);
    }

    public void init(Vector3 axis, float angle)
    {
        float sinHalfAngle = (float) Math.sin(angle / 2);
        float cosHalfAngle = (float) Math.cos(angle / 2);

        this.x = axis.getX() * sinHalfAngle;
        this.y = axis.getY() * sinHalfAngle;
        this.z = axis.getZ() * sinHalfAngle;
        this.w = cosHalfAngle;
    }

    public boolean isNull()
    {
        return x == 0 && y == 0 && z == 0 && w == 0;
    }

}
