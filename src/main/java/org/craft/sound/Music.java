package org.craft.sound;

import java.net.*;

public class Music
{

    private float     volume;
    private float     pitch;
    private AudioInfo infos;

    public Music(AudioInfo infos, float volume)
    {
        this(infos, volume, 1);
    }

    public Music(AudioInfo infos, float volume, float pitch)
    {
        this.infos = infos;
        this.volume = volume;
        this.pitch = pitch;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getVolume()
    {
        return volume;
    }

    public URL getURL()
    {
        return infos.getURL();
    }

    public String getID()
    {
        return infos.getID();
    }

    public String getFileIdentifier()
    {
        return infos.getFileIdentifier();
    }
}
