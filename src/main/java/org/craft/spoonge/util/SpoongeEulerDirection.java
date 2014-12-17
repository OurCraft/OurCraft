package org.craft.spoonge.util;

import com.flowpowered.math.vector.*;

import org.craft.maths.*;
import org.spongepowered.api.util.*;

public class SpoongeEulerDirection implements EulerDirection
{

    private static final long serialVersionUID = -5929666419226928759L;

    private float             roll;
    private float             pitch;
    private float             yaw;

    public SpoongeEulerDirection(float yaw, float pitch, float roll)
    {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    @Override
    public float getPitch()
    {
        return pitch;
    }

    @Override
    public float getYaw()
    {
        return yaw;
    }

    @Override
    public float getRoll()
    {
        return roll;
    }

    @Override
    public Vector3f toVector()
    {
        Quaternion q = new Quaternion(Vector3.xAxis, pitch).mul(new Quaternion(Vector3.yAxis, yaw)).mul(new Quaternion(Vector3.zAxis, roll));
        Vector3 v = q.getForward();
        return new Vector3f(v.getX(), v.getY(), v.getZ());
    }

    @Override
    public EulerDirection clone()
    {
        return new SpoongeEulerDirection(yaw, pitch, roll);
    }

}
