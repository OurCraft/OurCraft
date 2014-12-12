package org.craft;

import org.craft.world.*;

public interface IParticleHandler
{

    void spawnParticle(String test, float x, float y, float z);

    void spawnParticle(Particle particle);

    void updateAllParticles();
}
