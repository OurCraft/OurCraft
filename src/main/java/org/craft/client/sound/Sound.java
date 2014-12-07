package org.craft.client.sound;

import java.util.*;

import org.craft.client.*;
import org.craft.maths.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.utils.io.IOUtils;
import org.lwjgl.openal.*;

public class Sound implements IDisposable
{

    private static HashMap<AbstractResource, SoundResource> loadedResources = new HashMap<AbstractResource, SoundResource>();
    private SoundResource                                   data;
    private AbstractResource                                res;
    private int[]                                           sources;
    private int                                             sourceIndex     = 0;

    public Sound(AbstractResource res)
    {
        this(res, 8);
    }

    public Sound(AbstractResource res, int buffers)
    {
        sources = new int[Math.min(buffers, 8)];
        this.res = res;
        SoundResource existingResource = loadedResources.get(res);

        if(existingResource != null)
        {
            data = existingResource;
            data.increaseCounter();
        }
        else
        {
            data = OurCraft.getOurCraft().getSoundEngine().loadSound(res);
            loadedResources.put(res, data);
        }
    }

    @Override
    public void dispose()
    {
        if(data.decreaseCounter())
        {
            data.dispose();
            if(res != null)
                loadedResources.remove(res);
        }
    }

    public int play()
    {
        nextSoundIndex();
        printIfError();
        AL10.alSourcePlay(sources[sourceIndex]);
        return sources[sourceIndex];
    }

    int nextSoundIndex()
    {
        sourceIndex++ ;
        if(sourceIndex >= sources.length)
        {
            sourceIndex = 0;
        }
        if(sources[sourceIndex] == 0)
            sources[sourceIndex] = data.createSourceID();
        return sources[sourceIndex];
    }

    public Sound rewind(int source)
    {
        AL10.alSourceRewind(source);
        printIfError();
        return this;
    }

    public Sound rewind()
    {
        return rewind(sources[sourceIndex]);
    }

    public Sound resume(int source)
    {
        AL10.alSourcePlay(source);
        printIfError();
        return this;
    }

    public Sound resume()
    {
        return resume(sources[sourceIndex]);
    }

    public Sound pause(int source)
    {
        if(isPlaying(source))
            AL10.alSourcePause(source);
        printIfError();
        return this;
    }

    public Sound pause()
    {
        return pause(sources[sourceIndex]);
    }

    public Sound stop(int source)
    {
        if(isPlaying(source))
            AL10.alSourceStop(source);
        printIfError();
        return this;
    }

    public Sound stop()
    {
        return stop(sources[sourceIndex]);
    }

    private static void printIfError()
    {
        int error = AL10.alGetError();
        if(error != AL10.AL_NO_ERROR)
        {
            Log.error(SoundEngine.getALErrorString(error));
        }
    }

    public Sound setSourcePosition(int id, Vector3 pos)
    {
        AL10.alSource(id, AL10.AL_POSITION, IOUtils.createFlippedBuffer(pos));
        return this;
    }

    public Sound setSourceVelocity(int id, Vector3 vel)
    {
        AL10.alSource(id, AL10.AL_VELOCITY, IOUtils.createFlippedBuffer(vel));
        return this;
    }

    public boolean isPlaying()
    {
        return isPlaying(sources[sourceIndex]);
    }

    public boolean isPlaying(int source)
    {
        return AL10.alGetSourcei(source, AL10.AL_PLAYING) != 0;
    }

    public Sound setLooping(boolean loop)
    {
        for(int source : sources)
        {
            if(source != 0)
                AL10.alSourcei(source, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
        }
        return this;
    }

    public Sound setGain(float gain)
    {
        for(int source : sources)
        {
            if(source != 0)
                AL10.alSourcef(source, AL10.AL_GAIN, gain);
        }
        return this;
    }
}
