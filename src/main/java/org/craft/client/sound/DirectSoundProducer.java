package org.craft.client.sound;

import de.cuina.fireandfuel.*;

import org.craft.maths.*;
import org.craft.sound.*;
import org.craft.utils.*;
import org.craft.world.*;

import paulscode.sound.*;

public class DirectSoundProducer implements ISoundProducer
{

    private SoundSystem sndSystem;

    public DirectSoundProducer()
    {
        try
        {
            SoundSystemConfig.addLibrary(paulscode.sound.libraries.LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", paulscode.sound.codecs.CodecJOrbis.class);
            SoundSystemConfig.setCodec("wav", paulscode.sound.codecs.CodecWav.class);
            SoundSystemConfig.setCodec("mp3", CodecJLayerMP3.class);
            SoundSystemConfig.setDefaultFadeDistance(100);
        }
        catch(Throwable throwable)
        {
            Log.error("Error linking with the LibraryOpenAL plug-in", throwable);
        }
        sndSystem = new SoundSystem();
    }

    @Override
    public void playSound(String id, World w, float x, float y, float z)
    {
        playSound(new Sound(Sounds.getRandom(id), 1, w, x, y, z));
    }

    @Override
    public void playSound(Sound sound)
    {
        sndSystem.newStreamingSource(false, sound.getSourceName(), sound.getURL(), sound.getFileIdentifier(), false, sound.getX(), sound.getY(), sound.getZ(), 0, 0);
        sndSystem.setVolume(sound.getSourceName(), sound.getVolume());
        sndSystem.setPitch(sound.getSourceName(), sound.getPitch());
        sndSystem.play(sound.getSourceName());
    }

    public void setListenerOrientation(Quaternion q)
    {
        float lookX = q.getForward().negative().getX();
        float lookY = q.getForward().negative().getY();
        float lookZ = q.getForward().negative().getZ();

        float upX = q.getUp().getX();
        float upY = q.getUp().getY();
        float upZ = q.getUp().getZ();
        sndSystem.setListenerOrientation(lookX, lookY, lookZ, upX, upY, upZ);
    }

    public void setListenerLocation(float x, float y, float z)
    {
        sndSystem.setListenerPosition(x, y, z);
    }
}
