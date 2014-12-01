package org.craft.spongeimpl.math;

import org.craft.maths.*;
import org.spongepowered.api.math.*;

public class SpongeEulerDirection implements EulerDirection
{

    private static final long serialVersionUID = 2611648956254518238L;
    private float             yaw;
    private float             pitch;
    private float             roll;

    public SpongeEulerDirection(float yaw, float pitch, float roll)
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
        Quaternion q = new Quaternion(Vector3.xAxis, (float) Math.toRadians(yaw));
        q = q.mul(new Quaternion(Vector3.yAxis, (float) Math.toRadians(yaw)));
        Vector3 f = q.getForward();
        return Vectors.create3f(f.getX(), f.getY(), f.getZ());
    }

    @Override
    public EulerDirection clone()
    {
        return new SpongeEulerDirection(yaw, pitch, roll);
    }

}
