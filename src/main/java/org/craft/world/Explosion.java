package org.craft.world;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.entity.*;
import org.craft.maths.*;

public class Explosion implements Runnable, ILocatable
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

    public Explosion(ILocatable loc)
    {
        this(loc.getWorld(), loc.getPosX(), loc.getPosY(), loc.getPosZ());
    }

    public Explosion(ILocatable loc, float power)
    {
        this(loc.getWorld(), loc.getPosX(), loc.getPosY(), loc.getPosZ(), power);
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

    /**
     * Reference: <a href="http://minecraft.gamepedia.com/Explosion">Minecraft's explosion algorithm</a>
     */
    public void run()
    {
        List<Vector3> affectedBlocks = Lists.newArrayList();
        float gridSize = 16f;
        for(int gridX = 0; gridX <= gridSize; gridX++ )
        {
            for(int gridY = 0; gridY <= gridSize; gridY++ )
            {
                for(int gridZ = 0; gridZ <= gridSize; gridZ++ )
                {
                    float endX = (float) gridX / gridSize;
                    float endY = (float) gridY / gridSize;
                    float endZ = (float) gridZ / gridSize;
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
                        blastResistance = Math.max(0, block.getExplosionResistance());
                        float attenuation = (0.3f * 0.75f + (blastResistance / 5.f) * 0.3f);
                        if(!affectedBlocks.contains(v))
                        {
                            affectedBlocks.add(v);

                            if(block != Blocks.air)
                            {
                                block.onDestroyedByExplosion(this, world, blockX, blockY, blockZ);
                                world.setBlock(blockX, blockY, blockZ, Blocks.air, false);
                                if(smoke)
                                {
                                    Vector3 dir = center.sub(blockX, blockY, blockZ).normalize();
                                    float vx = (float) (dir.getX() + Math.random() * 2f - 1f) * -0.005f;
                                    float vy = (float) (dir.getY() + Math.random() * -2f) * -0.01f;
                                    float vz = (float) (dir.getZ() + Math.random() * 2f - 1f) * -0.005f;
                                    dir.dispose();
                                    Particle particle = new Particle("smoke", world, blockX + 0.5f + (float) Math.random() - 0.5f, blockY + 0.5f + (float) Math.random() - 0.5f, blockZ + 0.5f + (float) Math.random() - 0.5f, vx, vy, vz, 120L);
                                    world.spawnParticle(particle);
                                }
                            }
                        }
                        else
                        {
                            v.dispose();
                        }
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
            Chunk c = world.getChunk((int) Math.floor(v.getX()), (int) Math.floor(v.getY()), (int) Math.floor(v.getZ()));
            if(c != null)
                c.markDirty();
            v.dispose();
        }
        List<Entity> within = world.getEntitiesInRadius(x, y, z, 2 * power);
        Vector3 center = Vector3.get(x, y, z);
        for(Entity e : within)
        {
            if(e instanceof IExplosiveEntity)
            {
                IExplosiveEntity explosive = (IExplosiveEntity) e;
                explosive.onExplosion(this, world, x, y, z);
            }
            else
            {
                Vector3 entPos = Vector3.get(e.posX, e.posY, e.posZ);
                float exposure = 0.5f;
                float dist = entPos.sub(center).length();
                float impact = (1f - dist / power / 2f) * exposure;
                float damage = (impact * impact + impact) * 8 * power + 1;

                Vector3 dir = entPos.sub(center).mul(exposure);
                e.velX = dir.getX();
                e.velY = dir.getY();
                e.velZ = dir.getZ();
                dir.dispose();
                entPos.dispose();
            }
        }
        center.dispose();
        world.playSound("explode", this);
    }

    @Override
    public World getWorld()
    {
        return world;
    }

    @Override
    public float getPosX()
    {
        return x;
    }

    @Override
    public float getPosY()
    {
        return y;
    }

    @Override
    public float getPosZ()
    {
        return z;
    }
}
