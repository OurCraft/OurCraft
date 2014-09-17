package org.craft.world;

public class ChunkCoord
{

    public int x;
    public int y;
    public int z;

    public ChunkCoord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Object o)
    {
        if(o instanceof ChunkCoord)
        {
            ChunkCoord other = (ChunkCoord)o;
            return other.x == x && other.y == y && other.z == z;
        }
        return false;
    }
}
