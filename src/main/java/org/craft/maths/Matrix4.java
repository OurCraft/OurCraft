package org.craft.maths;

import java.nio.*;

import org.craft.utils.*;

public final class Matrix4 extends AbstractReference implements IDisposable
{

    private float[][] m;

    public Matrix4()
    {
        m = new float[4][4];
    }

    public float[][] getM()
    {
        return m;
    }

    public float get(int x, int y)
    {
        return m[x][y];
    }

    public void set(int x, int y, float value)
    {
        m[x][y] = value;
    }

    public Matrix4 mul(Matrix4 r)
    {
        Matrix4 res = new Matrix4();

        for(int i = 0; i < 4; i++ )
        {
            for(int j = 0; j < 4; j++ )
            {
                res.set(i, j, m[i][0] * r.get(0, j) + m[i][1] * r.get(1, j) + m[i][2] * r.get(2, j) + m[i][3] * r.get(3, j));
            }
        }

        return res;
    }

    public Matrix4 copy()
    {
        return new Matrix4().set(m);
    }

    public Matrix4 set(Matrix4 m)
    {
        for(int i = 0; i < 4; i++ )
        {
            for(int j = 0; j < 4; j++ )
            {
                this.m[i][j] = m.get(i, j);
            }
        }
        return this;
    }

    public Matrix4 set(float[][] m)
    {
        for(int i = 0; i < 4; i++ )
        {
            for(int j = 0; j < 4; j++ )
            {
                this.m[i][j] = m[i][j];
            }
        }
        return this;
    }

    public Matrix4 initIdentity()
    {
        m[0][0] = 1;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = 0;
        m[1][0] = 0;
        m[1][1] = 1;
        m[1][2] = 0;
        m[1][3] = 0;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = 1;
        m[2][3] = 0;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 initTranslation(float x, float y, float z)
    {
        m[0][0] = 1;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = x;
        m[1][0] = 0;
        m[1][1] = 1;
        m[1][2] = 0;
        m[1][3] = y;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = 1;
        m[2][3] = z;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 initRotation(float x, float y, float z)
    {
        Matrix4 rx = new Matrix4();
        Matrix4 ry = new Matrix4();
        Matrix4 rz = new Matrix4();

        x = (float) Math.toRadians(x);
        y = (float) Math.toRadians(y);
        z = (float) Math.toRadians(z);

        rz.m[0][0] = (float) Math.cos(z);
        rz.m[0][1] = (float) -Math.sin(z);
        rz.m[0][2] = 0;
        rz.m[0][3] = 0;
        rz.m[1][0] = (float) Math.sin(z);
        rz.m[1][1] = (float) Math.cos(z);
        rz.m[1][2] = 0;
        rz.m[1][3] = 0;
        rz.m[2][0] = 0;
        rz.m[2][1] = 0;
        rz.m[2][2] = 1;
        rz.m[2][3] = 0;
        rz.m[3][0] = 0;
        rz.m[3][1] = 0;
        rz.m[3][2] = 0;
        rz.m[3][3] = 1;

        rx.m[0][0] = 1;
        rx.m[0][1] = 0;
        rx.m[0][2] = 0;
        rx.m[0][3] = 0;
        rx.m[1][0] = 0;
        rx.m[1][1] = (float) Math.cos(x);
        rx.m[1][2] = (float) -Math.sin(x);
        rx.m[1][3] = 0;
        rx.m[2][0] = 0;
        rx.m[2][1] = (float) Math.sin(x);
        rx.m[2][2] = (float) Math.cos(x);
        rx.m[2][3] = 0;
        rx.m[3][0] = 0;
        rx.m[3][1] = 0;
        rx.m[3][2] = 0;
        rx.m[3][3] = 1;

        ry.m[0][0] = (float) Math.cos(y);
        ry.m[0][1] = 0;
        ry.m[0][2] = (float) -Math.sin(y);
        ry.m[0][3] = 0;
        ry.m[1][0] = 0;
        ry.m[1][1] = 1;
        ry.m[1][2] = 0;
        ry.m[1][3] = 0;
        ry.m[2][0] = (float) Math.sin(y);
        ry.m[2][1] = 0;
        ry.m[2][2] = (float) Math.cos(y);
        ry.m[2][3] = 0;
        ry.m[3][0] = 0;
        ry.m[3][1] = 0;
        ry.m[3][2] = 0;
        ry.m[3][3] = 1;

        m = rz.mul(ry.mul(rx)).getM();

        return this;
    }

    public Matrix4 initScale(float x, float y, float z)
    {
        m[0][0] = x;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = 0;
        m[1][0] = 0;
        m[1][1] = y;
        m[1][2] = 0;
        m[1][3] = 0;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = z;
        m[2][3] = 0;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 initPerspective(float fov, float aspectRatio, float zNear, float zFar)
    {
        float tanHalfFOV = (float) Math.tan(fov / 2);
        float zRange = zNear - zFar;

        m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = 0;
        m[1][0] = 0;
        m[1][1] = 1.0f / tanHalfFOV;
        m[1][2] = 0;
        m[1][3] = 0;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = (-zNear - zFar) / zRange;
        m[2][3] = 2 * zFar * zNear / zRange;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 1;
        m[3][3] = 0;

        return this;
    }

    public Matrix4 initOrthographic(float left, float right, float bottom, float top, float near, float far)
    {
        float width = right - left;
        float height = top - bottom;
        float depth = far - near;

        m[0][0] = 2 / width;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = -(right + left) / width;
        m[1][0] = 0;
        m[1][1] = 2 / height;
        m[1][2] = 0;
        m[1][3] = -(top + bottom) / height;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = -2 / depth;
        m[2][3] = -(far + near) / depth;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 initRotation(Vector3 forward, Vector3 up)
    {
        Vector3 f = forward.normalize();
        Vector3 r = f.cross(up).normalize();
        Vector3 u = up.normalize();
        return initRotation(f, u, r);
    }

    public Matrix4 initRotation(Vector3 forward, Vector3 up, Vector3 right)
    {
        Vector3 f = forward;
        Vector3 r = right;
        Vector3 u = up;

        m[0][0] = r.getX();
        m[0][1] = r.getY();
        m[0][2] = r.getZ();
        m[0][3] = 0;
        m[1][0] = u.getX();
        m[1][1] = u.getY();
        m[1][2] = u.getZ();
        m[1][3] = 0;
        m[2][0] = f.getX();
        m[2][1] = f.getY();
        m[2][2] = f.getZ();
        m[2][3] = 0;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Vector3 transform(Vector3 r)
    {
        return Vector3.get(m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3], m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3], m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3]);
    }

    public Quaternion transform(Quaternion r)
    {
        return new Quaternion(m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3] * r.getW(), m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3] * r.getW(), m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3] * r.getW(), m[3][0] * r.getX() + m[3][1] * r.getY() + m[3][2]
                * r.getZ() + m[3][3] * r.getW());
    }

    public void write(FloatBuffer buffer)
    {
        for(int i = 0; i < 4; i++ )
        {
            for(int j = 0; j < 4; j++ )
            {
                buffer.put((float) get(i, j));
            }
        }
    }

    public int getSize()
    {
        return 4 * 4;
    }

    public Matrix4 rotate()
    {
        Matrix4 result = new Matrix4();
        for(int i = 0; i < 4; i++ )
        {
            for(int j = 0; j < 4; j++ )
            {
                result.set(j, i, get(i, j));
            }
        }
        return result;
    }

    public void translate(float x, float y, float z)
    {
        this.set(add(initTranslation(x, y, z)));
    }

    public Matrix4 add(Matrix4 m)
    {
        Matrix4 result = new Matrix4();
        for(int i = 0; i < 4; i++ )
        {
            for(int j = 0; j < 4; j++ )
            {
                result.set(i, j, get(i, j) + m.get(i, j));
            }
        }
        return result;
    }

    public void rotate(Vector3 axis, float radians)
    {
        this.set(this.mul(new Quaternion(axis, radians).toRotationMatrix()));
    }

    private static ReferencedObjectPool<Matrix4> pool = ReferencedObjectPool.of(Matrix4.class);

    public static Matrix4 get()
    {
        return pool.get();
    }

    @Override
    public void dispose()
    {
        pool.dispose(this);
    }
}
