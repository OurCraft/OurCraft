package org.craft.maths;

import org.craft.utils.*;

public class AABB implements IDisposable
{

    private Vector3   minExtents;
    private Vector3   maxExtents;
    private Vector3[] vertices;

    /**
     * Creates a new AABB from given min extents and max extents
     */
    public AABB(Vector3 minExtents, Vector3 maxExtents)
    {
        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
        vertices = new Vector3[8];
        vertices[0] = minExtents;
        vertices[1] = minExtents.add(maxExtents.getX() - minExtents.getX(), 0, 0);
        vertices[2] = minExtents.add(0, maxExtents.getY() - minExtents.getY(), 0);
        vertices[3] = minExtents.add(0, 0, maxExtents.getZ() - minExtents.getZ());
        vertices[4] = maxExtents.sub(0, 0, maxExtents.getZ() - minExtents.getZ());
        vertices[5] = maxExtents.sub(0, maxExtents.getY() - minExtents.getY(), 0);
        vertices[6] = maxExtents.sub(maxExtents.getX() - minExtents.getX(), 0, 0);
        vertices[7] = maxExtents;
    }

    /**
     * Returns infos from intersection between this AABB and given one
     */
    public boolean intersectAABB(AABB other)
    {
        Vector3 dist1 = other.getMinExtents().sub(getMaxExtents());
        Vector3 dist2 = getMinExtents().sub(other.getMaxExtents());
        Vector3 dist = Vector3.max(dist1, dist2);
        float maxDistance = dist.max();

        return maxDistance <= 0.f;
    }

    public Vector3 getMinExtents()
    {
        return minExtents;
    }

    public Vector3 getMaxExtents()
    {
        return maxExtents;
    }

    /**
     * Creates a new AABB translated to given position
     */
    public AABB translate(float x, float y, float z)
    {
        return new AABB(minExtents.add(x, y, z), maxExtents.add(x, y, z));
    }

    public AABB translate(Vector3 position)
    {
        return new AABB(minExtents.add(position), maxExtents.add(position));
    }

    @Override
    public void dispose()
    {
        minExtents.dispose();
        maxExtents.dispose();
        for(Vector3 v : vertices)
            v.dispose();
    }

    public Vector3 getVertex(int i)
    {
        return vertices[i];
    }
}
