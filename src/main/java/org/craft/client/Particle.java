package org.craft.client;

public class Particle
{

    private String name;
    private float  x;
    private float  y;
    private float  z;
    private long   life;

    public Particle(String name, float x, float y, float z, long life)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
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
        y += 0.025f;
    }

    public boolean shouldBeKilled()
    {
        return life <= 0;
    }
}
