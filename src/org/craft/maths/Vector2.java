package org.craft.maths;

import java.nio.*;

public class Vector2
{

    public static final Vector2 NULL = new Vector2(0, 0);
    public double               x;
    public double               y;

    public Vector2(double x, double y)
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
        double l = length();
        double _x = this.x / l;
        double _y = this.y / l;
        return new Vector2(_x, _y);
    }

    /**
     * @param v
     */
    public double dot(Vector2 r)
    {
        return x * r.x + y * r.y;
    }

    public Vector2 add(double factor)
    {
        return new Vector2(this.x + factor, this.y + factor);
    }

    public Vector2 add(double x, double y)
    {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 add(Vector2 v)
    {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    public Vector2 sub(double factor)
    {
        return new Vector2(this.x - factor, this.y - factor);
    }

    public Vector2 sub(double x, double y)
    {
        return new Vector2(this.x - x, this.y - y);
    }

    public Vector2 sub(Vector2 v)
    {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    public Vector2 div(double factor)
    {
        return new Vector2(this.x / factor, this.y / factor);
    }

    public Vector2 div(double x, double y)
    {
        return new Vector2(this.x / x, this.y / y);
    }

    public Vector2 div(Vector2 v)
    {
        return new Vector2(this.x / v.x, this.y / v.y);
    }

    public Vector2 mul(double factor)
    {
        return new Vector2(this.x * factor, this.y * factor);
    }

    public Vector2 mul(Vector2 v)
    {
        return new Vector2(this.x * v.x, this.y * v.y);
    }

    public Vector2 mul(double x, double y)
    {
        return new Vector2(this.x * x, this.y * y);
    }

    public double length()
    {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 set(double x, double y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void write(FloatBuffer buffer)
    {
        buffer.put((float)getX());
        buffer.put((float)getY());
    }

    public int getSize()
    {
        return 2;
    }

    public double cross(Vector2 other)
    {
        return x * other.getY() - y * other.getX();
    }

    public Vector2 copy()
    {
        return new Vector2(x, y);
    }

    public Vector2 negative()
    {
        return new Vector2(-x, -y);
    }

    public Vector2 lerp(Vector2 dest, double factor)
    {
        return dest.sub(this).mul(factor).add(this);
    }

    public boolean equals(Object o)
    {
        if(o instanceof Vector2)
        {
            Vector2 other = (Vector2)o;
            return x == other.getX() && y == other.getY();
        }
        return false;
    }

    public double max()
    {
        return Math.max(getX(), getY());
    }
}
