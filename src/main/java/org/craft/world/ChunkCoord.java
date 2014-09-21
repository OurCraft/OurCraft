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

    public int hashCode()
    {
        final int BASE = 17;
        final int MULTIPLIER = 31;

        int result = BASE;
        result = MULTIPLIER * result + x;
        result = MULTIPLIER * result + y;
        result = MULTIPLIER * result + z;
        return result;
    }

    public boolean equals(Object o)
    {
        if(o instanceof ChunkCoord)
        {
            ChunkCoord other = (ChunkCoord) o;
            return other.x == x && other.y == y && other.z == z;
        }
        return false;
    }
}
