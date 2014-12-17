package org.craft.client.sound;

import java.util.*;

import com.google.common.collect.*;

import de.cuina.fireandfuel.*;

import org.craft.client.*;
import org.craft.maths.*;
import org.craft.sound.*;
import org.craft.utils.*;
import org.craft.world.*;

import paulscode.sound.*;

public class DirectSoundProducer implements IAudioHandler
{

    private SoundSystem        sndSystem;
    private List<Sound>        playingSounds;
    public static final String BACKGROUND_MUSIC = "backMusic";

    public DirectSoundProducer()
    {
        try
        {
            playingSounds = Lists.newArrayList();
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
        playSound(new Sound(AudioRegistry.getRandom(id), 1, w, x, y, z));
    }

    @Override
    public void playSound(Sound sound)
    {
        sndSystem.newStreamingSource(false, sound.getSourceName(), sound.getURL(), sound.getFileIdentifier(), false, sound.getPosX(), sound.getPosY(), sound.getPosZ(), 0, 0);
        sndSystem.setVolume(sound.getSourceName(), sound.getVolume());
        sndSystem.setPitch(sound.getSourceName(), sound.getPitch());
        sndSystem.play(sound.getSourceName());
        playingSounds.add(sound);
    }

    public void playMusic(Music music)
    {
        playMusicWithID(music, music.getID());
    }

    public void playMusic(String id, float volume)
    {
        playMusic(new Music(AudioRegistry.getRandom(id), volume));
    }

    public void playBackgroundMusic(Music music)
    {
        playMusicWithID(music, BACKGROUND_MUSIC);
    }

    private void playMusicWithID(Music music, String id)
    {
        sndSystem.newStreamingSource(true, id, music.getURL(), music.getFileIdentifier(), false, 0, 0, 0, SoundSystemConfig.ATTENUATION_ROLLOFF, 0);
        sndSystem.setVolume(id, music.getVolume());
        sndSystem.setPitch(id, music.getPitch());
        sndSystem.play(id);
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

    public void setListenerLocation(ILocatable loc)
    {
        setListenerLocation(loc.getPosX(), loc.getPosY(), loc.getPosZ());
    }

    public void setListenerLocation(float x, float y, float z)
    {
        sndSystem.setListenerPosition(x, y, z);
    }

    public void playBackgroundMusic(String string)
    {
        playBackgroundMusic(AudioRegistry.getFirst(string));
    }

    public void playBackgroundMusic(AudioInfo audio)
    {
        playBackgroundMusic(new Music(audio, OurCraft.getOurCraft().getGameSettings().musicVolume.getValue()));
    }

    public boolean isMusicPlaying()
    {
        return sndSystem.playing(BACKGROUND_MUSIC);
    }

    public void setSoundVolume(float volume)
    {
        List<Sound> toRemove = Lists.newArrayList();
        for(int i = 0; i < playingSounds.size(); i++ )
        {
            Sound playing = playingSounds.get(i);
            if(playing == null)
                continue;
            sndSystem.setVolume(playing.getSourceName(), volume);
            if(!isPlaying(playing.getSourceName()))
                toRemove.add(playing);
        }
        playingSounds.removeAll(toRemove);
    }

    public boolean isPlaying(String sourceName)
    {
        return sndSystem.playing(sourceName);
    }

    public void setMusicVolume(float volume)
    {
        sndSystem.setVolume(BACKGROUND_MUSIC, volume);
    }

    public void setMasterVolume(float volume)
    {
        sndSystem.setMasterVolume(volume);
    }

    @Override
    public void playSound(String id, ILocatable location)
    {
        playSound(id, location.getWorld(), location.getPosX(), location.getPosY(), location.getPosZ());
    }
}
