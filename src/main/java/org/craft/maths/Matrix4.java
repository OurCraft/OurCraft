package org.craft.maths;

import java.nio.*;
import java.util.*;

import org.craft.utils.*;

public class Matrix4 extends AbstractReference implements IDisposable
{

    public static final Matrix4 TMP = new Matrix4();
    private float[][]           _m;

    private Matrix4()
    {
        _m = new float[4][4];
    }

    public float[][] getM()
    {
        return _m;
    }

    public float get(int x, int y)
    {
        return _m[x][y];
    }

    public void set(int x, int y, float value)
    {
        _m[x][y] = value;
    }

    public Matrix4 mul(Matrix4 r)
    {
        Matrix4 res = new Matrix4();

        for(int i = 0; i < 4; i++ )
        {
            for(int j = 0; j < 4; j++ )
            {
                res.set(i, j, _m[i][0] * r.get(0, j) + _m[i][1] * r.get(1, j) + _m[i][2] * r.get(2, j) + _m[i][3] * r.get(3, j));
            }
        }

        return res;
    }

    public Matrix4 copy()
    {
        return new Matrix4().set(_m);
    }

    public Matrix4 set(Matrix4 m)
    {
        for(int i = 0; i < 4; i++ )
        {
            for(int j = 0; j < 4; j++ )
            {
                this._m[i][j] = m.get(i, j);
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
                this._m[i][j] = m[i][j];
            }
        }
        return this;
    }

    public Matrix4 initIdentity()
    {
        _m[0][0] = 1;
        _m[0][1] = 0;
        _m[0][2] = 0;
        _m[0][3] = 0;
        _m[1][0] = 0;
        _m[1][1] = 1;
        _m[1][2] = 0;
        _m[1][3] = 0;
        _m[2][0] = 0;
        _m[2][1] = 0;
        _m[2][2] = 1;
        _m[2][3] = 0;
        _m[3][0] = 0;
        _m[3][1] = 0;
        _m[3][2] = 0;
        _m[3][3] = 1;

        return this;
    }

    public Matrix4 initTranslation(float x, float y, float z)
    {
        _m[0][0] = 1;
        _m[0][1] = 0;
        _m[0][2] = 0;
        _m[0][3] = x;
        _m[1][0] = 0;
        _m[1][1] = 1;
        _m[1][2] = 0;
        _m[1][3] = y;
        _m[2][0] = 0;
        _m[2][1] = 0;
        _m[2][2] = 1;
        _m[2][3] = z;
        _m[3][0] = 0;
        _m[3][1] = 0;
        _m[3][2] = 0;
        _m[3][3] = 1;

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

        rz._m[0][0] = (float) Math.cos(z);
        rz._m[0][1] = (float) -Math.sin(z);
        rz._m[0][2] = 0;
        rz._m[0][3] = 0;
        rz._m[1][0] = (float) Math.sin(z);
        rz._m[1][1] = (float) Math.cos(z);
        rz._m[1][2] = 0;
        rz._m[1][3] = 0;
        rz._m[2][0] = 0;
        rz._m[2][1] = 0;
        rz._m[2][2] = 1;
        rz._m[2][3] = 0;
        rz._m[3][0] = 0;
        rz._m[3][1] = 0;
        rz._m[3][2] = 0;
        rz._m[3][3] = 1;

        rx._m[0][0] = 1;
        rx._m[0][1] = 0;
        rx._m[0][2] = 0;
        rx._m[0][3] = 0;
        rx._m[1][0] = 0;
        rx._m[1][1] = (float) Math.cos(x);
        rx._m[1][2] = (float) -Math.sin(x);
        rx._m[1][3] = 0;
        rx._m[2][0] = 0;
        rx._m[2][1] = (float) Math.sin(x);
        rx._m[2][2] = (float) Math.cos(x);
        rx._m[2][3] = 0;
        rx._m[3][0] = 0;
        rx._m[3][1] = 0;
        rx._m[3][2] = 0;
        rx._m[3][3] = 1;

        ry._m[0][0] = (float) Math.cos(y);
        ry._m[0][1] = 0;
        ry._m[0][2] = (float) -Math.sin(y);
        ry._m[0][3] = 0;
        ry._m[1][0] = 0;
        ry._m[1][1] = 1;
        ry._m[1][2] = 0;
        ry._m[1][3] = 0;
        ry._m[2][0] = (float) Math.sin(y);
        ry._m[2][1] = 0;
        ry._m[2][2] = (float) Math.cos(y);
        ry._m[2][3] = 0;
        ry._m[3][0] = 0;
        ry._m[3][1] = 0;
        ry._m[3][2] = 0;
        ry._m[3][3] = 1;

        _m = rz.mul(ry.mul(rx)).getM();

        return this;
    }

    public Matrix4 initScale(float x, float y, float z)
    {
        _m[0][0] = x;
        _m[0][1] = 0;
        _m[0][2] = 0;
        _m[0][3] = 0;
        _m[1][0] = 0;
        _m[1][1] = y;
        _m[1][2] = 0;
        _m[1][3] = 0;
        _m[2][0] = 0;
        _m[2][1] = 0;
        _m[2][2] = z;
        _m[2][3] = 0;
        _m[3][0] = 0;
        _m[3][1] = 0;
        _m[3][2] = 0;
        _m[3][3] = 1;

        return this;
    }

    public Matrix4 initPerspective(float fov, float aspectRatio, float zNear, float zFar)
    {
        float tanHalfFOV = (float) Math.tan(fov / 2);
        float zRange = zNear - zFar;

        _m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);
        _m[0][1] = 0;
        _m[0][2] = 0;
        _m[0][3] = 0;
        _m[1][0] = 0;
        _m[1][1] = 1.0f / tanHalfFOV;
        _m[1][2] = 0;
        _m[1][3] = 0;
        _m[2][0] = 0;
        _m[2][1] = 0;
        _m[2][2] = (-zNear - zFar) / zRange;
        _m[2][3] = 2 * zFar * zNear / zRange;
        _m[3][0] = 0;
        _m[3][1] = 0;
        _m[3][2] = 1;
        _m[3][3] = 0;

        return this;
    }

    public Matrix4 initOrthographic(float left, float right, float bottom, float top, float near, float far)
    {
        float width = right - left;
        float height = top - bottom;
        float depth = far - near;

        _m[0][0] = 2 / width;
        _m[0][1] = 0;
        _m[0][2] = 0;
        _m[0][3] = -(right + left) / width;
        _m[1][0] = 0;
        _m[1][1] = 2 / height;
        _m[1][2] = 0;
        _m[1][3] = -(top + bottom) / height;
        _m[2][0] = 0;
        _m[2][1] = 0;
        _m[2][2] = -2 / depth;
        _m[2][3] = -(far + near) / depth;
        _m[3][0] = 0;
        _m[3][1] = 0;
        _m[3][2] = 0;
        _m[3][3] = 1;

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

        _m[0][0] = r.getX();
        _m[0][1] = r.getY();
        _m[0][2] = r.getZ();
        _m[0][3] = 0;
        _m[1][0] = u.getX();
        _m[1][1] = u.getY();
        _m[1][2] = u.getZ();
        _m[1][3] = 0;
        _m[2][0] = f.getX();
        _m[2][1] = f.getY();
        _m[2][2] = f.getZ();
        _m[2][3] = 0;
        _m[3][0] = 0;
        _m[3][1] = 0;
        _m[3][2] = 0;
        _m[3][3] = 1;

        return this;
    }

    public Vector3 transform(Vector3 r)
    {
        return Vector3.get(_m[0][0] * r.getX() + _m[0][1] * r.getY() + _m[0][2] * r.getZ() + _m[0][3], _m[1][0] * r.getX() + _m[1][1] * r.getY() + _m[1][2] * r.getZ() + _m[1][3], _m[2][0] * r.getX() + _m[2][1] * r.getY() + _m[2][2] * r.getZ() + _m[2][3]);
    }

    public Quaternion transform(Quaternion r)
    {
        return new Quaternion(_m[0][0] * r.getX() + _m[0][1] * r.getY() + _m[0][2] * r.getZ() + _m[0][3] * r.getW(), _m[1][0] * r.getX() + _m[1][1] * r.getY() + _m[1][2] * r.getZ() + _m[1][3] * r.getW(), _m[2][0] * r.getX() + _m[2][1] * r.getY() + _m[2][2] * r.getZ() + _m[2][3] * r.getW(), _m[3][0] * r.getX() + _m[3][1] * r.getY() + _m[3][2]
                * r.getZ() + _m[3][3] * r.getW());
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
        this.set(this.add(TMP.initTranslation(x, y, z)));
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

    private static Stack<Matrix4> unused = new Stack<Matrix4>();

    public static Matrix4 get()
    {
        Matrix4 v = null;
        if(unused.isEmpty())
        {
            v = new Matrix4();
        }
        else
        {
            try
            {
                v = unused.pop();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                v = new Matrix4();
            }
        }
        v.increaseReferenceCounter();
        return v;
    }

    @Override
    public void dispose()
    {
        if(decreaseReferenceCounter())
        {
            unused.push(this);
        }
    }
}
