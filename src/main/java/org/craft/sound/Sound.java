package org.craft.sound;

import java.net.*;

import org.craft.world.*;

public class Sound implements ILocatable
{

    private float     x;
    private float     y;
    private float     z;
    private float     volume;
    private float     pitch;
    private World     world;
    private AudioInfo infos;

    public Sound(AudioInfo infos, float volume, World w, float x, float y, float z)
    {
        this(infos, volume, 1, w, x, y, z);
    }

    public Sound(AudioInfo infos, float volume, World w, ILocatable loc)
    {
        this(infos, volume, 1, w, loc.getPosX(), loc.getPosY(), loc.getPosZ());
    }

    public Sound(AudioInfo infos, float volume, float pitch, World w, ILocatable loc)
    {
        this(infos, volume, pitch, w, loc.getPosX(), loc.getPosY(), loc.getPosZ());
    }

    public Sound(AudioInfo infos, float volume, float pitch, World w, float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = w;
        this.volume = volume;
        this.pitch = pitch;
        this.infos = infos;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getVolume()
    {
        return volume;
    }

    public World getWorld()
    {
        return world;
    }

    public float getPosX()
    {
        return x;
    }

    public float getPosY()
    {
        return y;
    }

    public float getPosZ()
    {
        return z;
    }

    public URL getURL()
    {
        return infos.getURL();
    }

    public String getSourceName()
    {
        return infos.getID();
    }

    public String getFileIdentifier()
    {
        return infos.getFileIdentifier();
    }
}
