package org.craft.maths;

public class Frustum
{

    public final static int TOP    = 0;
    public final static int BOTTOM = 1;
    public final static int LEFT   = 2;
    public final static int RIGHT  = 3;
    public final static int NEAR   = 4;
    public final static int FAR    = 5;
    private Plane[]         planes;

    public Frustum()
    {
        planes = new Plane[6];
        for(int i = 0; i < planes.length; i++ )
            planes[i] = new Plane();
    }

    public void update(float radangle, float ratio, float nearD, float farD, Vector3 camPos, Vector3 camRot, Vector3 camUp)
    {
        float tang = (float) Math.tan(radangle * 0.5f);
        float nearPlaneH = nearD * tang;
        float nearPlaneW = nearPlaneH * ratio;

        float farPlaneH = farD * tang;
        float farPlaneW = farPlaneH * ratio;

        Vector3 nearCenter, farCenter, xAxis, yAxis, zAxis;

        zAxis = camPos.sub(1).normalize();

        xAxis = camUp.mul(zAxis).normalize();

        yAxis = zAxis.cross(xAxis);

        nearCenter = camPos.sub(zAxis.mul(nearD));

        farCenter = camPos.sub(zAxis.mul(farD));

        planes[NEAR].setNormalAndPoint(zAxis.negative(), nearCenter);
        planes[FAR].setNormalAndPoint(zAxis, farCenter);

        Vector3 aux, normal;

        aux = (nearCenter.add(yAxis.mul(nearPlaneH))).sub(camPos);
        aux.normalize();
        normal = aux.mul(xAxis);
        planes[TOP].setNormalAndPoint(normal, nearCenter.add(yAxis.mul(nearPlaneH)));

        aux = (nearCenter.sub(yAxis.mul(nearPlaneH))).sub(camPos);
        aux.normalize();
        normal = xAxis.mul(aux);
        planes[BOTTOM].setNormalAndPoint(normal, nearCenter.sub(yAxis.mul(nearPlaneH)));

        aux = (nearCenter.sub(xAxis.mul(nearPlaneW))).sub(camPos);
        aux.normalize();
        normal = aux.cross(yAxis);
        planes[LEFT].setNormalAndPoint(normal, nearCenter.sub(xAxis.mul(nearPlaneW)));

        aux = (nearCenter.add(xAxis.mul(nearPlaneW))).sub(camPos);
        aux.normalize();
        normal = yAxis.cross(aux);
        planes[RIGHT].setNormalAndPoint(normal, nearCenter.add(xAxis.mul(nearPlaneW)));

    }

    public boolean pointIn(Vector3 point)
    {
        for(Plane plane : planes)
        {
            if(plane.getDistance(point) < 0)
                return false;
        }
        return true;
    }

    public boolean sphereIn(Vector3 center, float radius)
    {
        for(Plane plane : planes)
        {
            if(plane.getDistance(center) < -radius)
                return false;
        }
        return true;
    }

    public boolean boxIn(AABB aabb)
    {
        int in, out;
        for(int i = 0; i < 6; i++ )
        {
            out = 0;
            in = 0;
            for(int pointIndex = 0; pointIndex < 8 && (in == 0 || out == 0); pointIndex++ )
            {
                if(planes[i].getDistance(aabb.getVertex(pointIndex)) < 0.f)
                    out++ ;
                else
                    in++ ;
            }
            if(in == 0)
                return false;
        }
        return true;
    }

}
