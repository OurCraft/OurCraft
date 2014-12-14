package org.craft.sound;

import org.craft.world.*;

public interface IAudioHandler
{

    void playSound(String id, World w, float x, float y, float z);

    void playSound(String id, ILocatable location);

    void playSound(Sound sound);

    void playMusic(String id, float volume);

    void playMusic(Music music);
}
