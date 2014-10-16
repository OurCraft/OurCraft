package org.craft.maths;

import java.nio.*;
import java.util.*;

import org.craft.utils.*;

public final class Vector2 extends AbstractReference implements IDisposable
{

    public static final Vector2 NULL = new Vector2(0, 0);
    public float                x;
    public float                y;

    private Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return "vec2(" + x + "," + y + ")";
    }

    public Vector2 normalize()
    {
        float l = length();
        float localX = this.x / l;
        float localY = this.y / l;
        return Vector2.get(localX, localY);
    }

    /**
     * @param v
     */
    public float dot(Vector2 r)
    {
        return x * r.x + y * r.y;
    }

    public Vector2 add(float factor)
    {
        return Vector2.get(this.x + factor, this.y + factor);
    }

    public Vector2 add(float x, float y)
    {
        return Vector2.get(this.x + x, this.y + y);
    }

    public Vector2 add(Vector2 v)
    {
        return Vector2.get(this.x + v.x, this.y + v.y);
    }

    public Vector2 sub(float factor)
    {
        return Vector2.get(this.x - factor, this.y - factor);
    }

    public Vector2 sub(float x, float y)
    {
        return Vector2.get(this.x - x, this.y - y);
    }

    public Vector2 sub(Vector2 v)
    {
        return Vector2.get(this.x - v.x, this.y - v.y);
    }

    public Vector2 div(float factor)
    {
        return Vector2.get(this.x / factor, this.y / factor);
    }

    public Vector2 div(float x, float y)
    {
        return Vector2.get(this.x / x, this.y / y);
    }

    public Vector2 div(Vector2 v)
    {
        return Vector2.get(this.x / v.x, this.y / v.y);
    }

    public Vector2 mul(float factor)
    {
        return Vector2.get(this.x * factor, this.y * factor);
    }

    public Vector2 mul(Vector2 v)
    {
        return Vector2.get(this.x * v.x, this.y * v.y);
    }

    public Vector2 mul(float x, float y)
    {
        return Vector2.get(this.x * x, this.y * y);
    }

    public float length()
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2 set(float x, float y)
    {
        this.x = x;
        this.y = y;
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

    public void write(FloatBuffer buffer)
    {
        buffer.put((float) getX());
        buffer.put((float) getY());
    }

    public int getSize()
    {
        return 2;
    }

    public float cross(Vector2 other)
    {
        return x * other.getY() - y * other.getX();
    }

    public Vector2 copy()
    {
        return Vector2.get(x, y);
    }

    public Vector2 negative()
    {
        return Vector2.get(-x, -y);
    }

    public Vector2 lerp(Vector2 dest, float factor)
    {
        return dest.sub(this).mul(factor).add(this);
    }

    public boolean equals(Object o)
    {
        if(o instanceof Vector2)
        {
            Vector2 other = (Vector2) o;
            return x == other.getX() && y == other.getY();
        }
        return false;
    }

    public float max()
    {
        return Math.max(getX(), getY());
    }

    @Override
    public void dispose()
    {
        if(decreaseReferenceCounter())
        {
            unused.push(this);
        }
    }

    private static Stack<Vector2> unused = new Stack<Vector2>();

    public static Vector2 get(float x, float y)
    {
        Vector2 v = null;
        if(unused.isEmpty())
        {
            v = new Vector2(x, y);
        }
        else
        {
            v = unused.pop();
            v.set(x, y);
        }
        v.increaseReferenceCounter();
        return v;
    }
}
