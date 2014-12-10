package org.craft.sound;

import java.net.*;

import org.craft.world.*;

public class Sound
{

    private String identifier;
    private String id;
    private URL    url;
    private float  x;
    private float  y;
    private float  z;
    private float  volume;
    private float  pitch;
    private World  world;

    public Sound(SoundInfo infos, float volume, World w, float x, float y, float z)
    {
        this(infos, volume, 1, w, x, y, z);
    }

    public Sound(SoundInfo infos, float volume, float pitch, World w, float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = w;
        this.volume = volume;
        this.pitch = pitch;
        this.url = infos.getURL();
        this.id = infos.getID();
        this.identifier = infos.getFileIdentifier();
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

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public URL getURL()
    {
        return url;
    }

    public String getSourceName()
    {
        return id;
    }

    public String getFileIdentifier()
    {
        return identifier;
    }
}
