package org.craft.spongeimpl.math;

import org.spongepowered.api.math.*;

public class SpongeEulerDirection implements EulerDirection
{

    private float yaw;
    private float pitch;
    private float roll;

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
        // TODO
        return null;
    }

    @Override
    public EulerDirection clone()
    {
        return new SpongeEulerDirection(yaw, pitch, roll);
    }

}
