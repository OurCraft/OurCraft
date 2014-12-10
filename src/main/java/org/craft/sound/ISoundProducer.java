package org.craft.sound;

import org.craft.world.*;

public interface ISoundProducer
{

    void playSound(String id, World w, float x, float y, float z);

    void playSound(Sound sound);
}
