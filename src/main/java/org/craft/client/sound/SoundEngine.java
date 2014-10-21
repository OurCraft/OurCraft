package org.craft.client.sound;

import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.lwjgl.*;
import org.lwjgl.openal.*;

public class SoundEngine
{

    private int        maxSourcesPerSound;
    private Quaternion rot;
    private Vector3    pos;

    public SoundEngine()
    {
        maxSourcesPerSound = 16;

        try
        {
            AL.create();
        }
        catch(LWJGLException e)
        {
            e.printStackTrace();
        }
    }

    public void init()
    {
    }

    public void update(double delta)
    {
        AL10.alListener3f(AL10.AL_POSITION, pos.getX(), pos.getY(), pos.getZ());
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
        AL10.alListener(AL10.AL_ORIENTATION, Buffers.createFlippedBuffer(rot.getForward().negative().normalize(), rot.getUp().normalize()));
    }

    public Vector3 getListenerPos()
    {
        return pos;
    }

    public void setListenerPos(Vector3 pos)
    {
        this.pos = pos;
    }

    public Quaternion getListenerRot()
    {
        return rot;
    }

    public void setListenerRot(Quaternion rot)
    {
        this.rot = rot;
    }

    public SoundResource loadSound(AbstractResource res)
    {
        String ext = res.getResourceLocation().getExtension().toUpperCase();
        SoundFormats format = SoundFormats.valueOf(ext);
        if(format == null)
            throw new RuntimeException("Unsupported sound format: " + ext);
        try
        {
            return format.getLoader().loadResource(res.getData());
        }
        catch(Exception e)
        {
            throw new RuntimeException("Error while loading sound " + res.getResourceLocation().getFullPath(), e);
        }
    }

    public String getOpenALVendor()
    {
        return AL10.alGetString(AL10.AL_VENDOR);
    }

    public String getOpenALVersion()
    {
        return AL10.alGetString(AL10.AL_VERSION);
    }

    public static String getALErrorString(int err)
    {
        switch(err)
        {
            case AL10.AL_NO_ERROR:
                return "AL_NO_ERROR";
            case AL10.AL_INVALID_NAME:
                return "AL_INVALID_NAME";
            case AL10.AL_INVALID_ENUM:
                return "AL_INVALID_ENUM";
            case AL10.AL_INVALID_VALUE:
                return "AL_INVALID_VALUE";
            case AL10.AL_INVALID_OPERATION:
                return "AL_INVALID_OPERATION";
            case AL10.AL_OUT_OF_MEMORY:
                return "AL_OUT_OF_MEMORY";
            default:
                return "No such error code";
        }
    }

    public int getMaxSourcesPerSound()
    {
        return maxSourcesPerSound;
    }

    public SoundEngine setMaxSourcesPerSound(int max)
    {
        this.maxSourcesPerSound = max;
        return this;
    }
}
