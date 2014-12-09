package org.craft.world;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.maths.*;

public class Explosion
{

    private World   world;
    private float   x;
    private float   y;
    private float   z;
    private float   power;
    private boolean smoke;

    public Explosion(World w, float x, float y, float z)
    {
        this(w, x, y, z, 1);
    }

    public Explosion(World w, float x, float y, float z, float power)
    {
        this.world = w;
        this.x = x;
        this.y = y;
        this.z = z;
        this.power = power;
    }

    public void producesSmoke(boolean smoke)
    {
        this.smoke = smoke;
    }

    public boolean producesSmoke()
    {
        return smoke;
    }

    public void perform()
    {
        List<Vector3> affectedBlocks = Lists.newArrayList();
        for(int gridX = 0; gridX <= 16; gridX++ )
        {
            for(int gridY = 0; gridY <= 16; gridY++ )
            {
                for(int gridZ = 0; gridZ <= 16; gridZ++ )
                {
                    float endX = (float) gridX / 16f;
                    float endY = (float) gridY / 16f;
                    float endZ = (float) gridZ / 16f;
                    Vector3 center = Vector3.get(0.5f, 0.5f, 0.5f);
                    Vector3 end = Vector3.get(endX, endY, endZ);
                    Vector3 initialRay = center.sub(end).normalize();
                    center.dispose();
                    end.dispose();
                    float intensity = (float) (0.4f * world.getRNG().nextFloat() * 0.6f * power);
                    for(float step = 0;; step += 0.5f)
                    {
                        Vector3 ray = initialRay.mul(step);
                        int blockX = (int) Math.floor(ray.getX() + x);
                        int blockY = (int) Math.floor(ray.getY() + y);
                        int blockZ = (int) Math.floor(ray.getZ() + z);
                        Vector3 v = Vector3.get(blockX, blockY, blockZ);
                        float blastResistance = 0f;
                        Block block = world.getBlockAt(blockX, blockY, blockZ);
                        if(!affectedBlocks.contains(v))
                        {
                            if(block != Blocks.air)
                            {
                                affectedBlocks.add(v);
                                world.setBlock(blockX, blockY, blockZ, Blocks.air);
                                if(smoke)
                                {
                                    Vector3 dir = center.sub(blockX, blockY, blockZ).normalize();
                                    float vx = (float) (dir.getX() + Math.random() * 2f - 1f) * -0.005f;
                                    float vy = (float) (dir.getY() + Math.random() * -2f) * -0.01f;
                                    float vz = (float) (dir.getZ() + Math.random() * 2f - 1f) * -0.005f;
                                    dir.dispose();
                                    Particle particle = new Particle("test", blockX + 0.5f + (float) Math.random() - 0.5f, blockY + 0.5f + (float) Math.random() - 0.5f, blockZ + 0.5f + (float) Math.random() - 0.5f, vx, vy, vz, 120L);
                                    world.spawnParticle(particle);
                                }
                            }
                        }
                        else
                        {
                            v.dispose();
                        }
                        blastResistance = block.getExplosionResistance(); // TODO: Per block resistance
                        float attenuation = (0.3f * 0.75f + (blastResistance / 5.f) * 0.3f);
                        intensity -= attenuation;
                        ray.dispose();
                        if(intensity < 0f)
                        {
                            break;
                        }
                    }
                    initialRay.dispose();
                }
            }
        }
        for(Vector3 v : affectedBlocks)
        {
            v.dispose();
        }
        //world.playSound("explode", x,y,z);
    }
}
