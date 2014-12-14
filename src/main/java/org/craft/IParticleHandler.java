package org.craft;

import org.craft.world.*;

public interface IParticleHandler
{

    void spawnParticle(String id, World w, float x, float y, float z);

    void spawnParticle(String id, ILocatable loc);

    void spawnParticle(Particle particle);

    void updateAllParticles();
}
