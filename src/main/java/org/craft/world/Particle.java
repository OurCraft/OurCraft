package org.craft.world;

public class Particle implements ILocatable
{

    private String name;
    private float  x;
    private float  y;
    private float  z;
    private float  vx;
    private float  vy;
    private float  vz;
    private long   life;
    private World  world;

    public Particle(String name, ILocatable loc, long life)
    {
        this(name, loc.getWorld(), loc.getPosX(), loc.getPosY(), loc.getPosZ(), life);
    }

    public Particle(String name, ILocatable loc, float vx, float vy, float vz, long life)
    {
        this(name, loc.getWorld(), loc.getPosX(), loc.getPosY(), loc.getPosZ(), vx, vy, vz, life);
    }

    public Particle(String name, World w, float x, float y, float z, long life)
    {
        this(name, w, x, y, z, 0, 0, 0, life);
    }

    public Particle(String name, World w, float x, float y, float z, float vx, float vy, float vz, long life)
    {
        this.name = name;
        this.world = w;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.life = life;
    }

    public String getName()
    {
        return name;
    }

    public float getPosX()
    {
        return x;
    }

    public float getPosY()
    {
        return y;
    }

    public float getPosZ()
    {
        return z;
    }

    public void update()
    {
        life-- ;
        x += vx;
        y += vy;
        z += vz;
    }

    public boolean shouldBeKilled()
    {
        return life <= 0;
    }

    @Override
    public World getWorld()
    {
        return world;
    }
}
