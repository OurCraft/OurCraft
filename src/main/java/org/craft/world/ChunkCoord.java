package org.craft.world;

import org.craft.utils.*;

public class ChunkCoord extends AbstractReference
{

    public int x;
    public int y;
    public int z;

    public ChunkCoord()
    {

    }

    public ChunkCoord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
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

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof ChunkCoord)
        {
            ChunkCoord other = (ChunkCoord) o;
            return other.x == x && other.y == y && other.z == z;
        }
        return false;
    }

    private static ReferencedObjectPool<ChunkCoord> pool = ReferencedObjectPool.of(ChunkCoord.class);

    /**
     * Returns a chunk coords instance from given coordinates
     */
    public static ChunkCoord get(int chunkX, int chunkY, int chunkZ)
    {
        ChunkCoord coord = pool.get();
        coord.x = chunkX;
        coord.y = chunkY;
        coord.z = chunkZ;
        return coord;
    }
}
