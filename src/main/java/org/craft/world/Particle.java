package org.craft.world;

public class Particle
{

    private String name;
    private float  x;
    private float  y;
    private float  z;
    private float  vx;
    private float  vy;
    private float  vz;
    private long   life;

    public Particle(String name, float x, float y, float z, long life)
    {
        this(name, x, y, z, 0, 0, 0, life);
    }

    public Particle(String name, float x, float y, float z, float vx, float vy, float vz, long life)
    {
        this.name = name;
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

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
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
}
