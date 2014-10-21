package org.craft.client.sound;

import java.util.*;

import org.craft.resources.*;
import org.lwjgl.openal.*;

/**
 * Needs to use streaming!!!!
 * 
 */
public class Music
{

    private static final HashMap<AbstractResource, Music> musics = new HashMap<AbstractResource, Music>();
    private Sound                                         sound;
    private int                                           sourceId;
    private long                                          startedTime;

    /**
     * Needs to use streaming!!!!
     * 
     */
    private Music(AbstractResource res)
    {
        musics.put(res, this);
        sound = new Sound(res, 1);
        sourceId = sound.nextSoundIndex();
    }

    public Music play()
    {
        if(!sound.isPlaying())
            startedTime = System.currentTimeMillis();
        sound.play();
        return this;
    }

    public Music resume()
    {
        sound.resume();
        return this;
    }

    public Music stop()
    {
        sound.stop();
        return this;
    }

    public Music pause()
    {
        sound.pause();
        return this;
    }

    public Music setGain(float gain)
    {
        sound.setGain(gain);
        return this;
    }

    public Music setPitch(float pitch)
    {
        AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
        return this;
    }

    public long getTimeSinceStarted()
    {
        return System.currentTimeMillis() - startedTime;
    }

    public static Music get(AbstractResource res)
    {
        if(musics.containsKey(res))
        {
            return musics.get(res);
        }
        Music mus = new Music(res);
        AL10.alSourcei(mus.sourceId, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);
        return mus;
    }

    public Music setLooping(boolean repeat)
    {
        sound.setLooping(repeat);
        return this;
    }

    public static ArrayList<Music> musicsPlaying()
    {
        Iterator<Music> it = musics.values().iterator();
        ArrayList<Music> playing = new ArrayList<Music>();
        while(it.hasNext())
        {
            Music mus = it.next();
            if(mus.isPlaying())
            {
                playing.add(mus);
            }
        }
        return playing;
    }

    public boolean isPlaying()
    {
        return sound.isPlaying();
    }
}
