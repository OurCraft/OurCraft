package org.craft.maths;

public class AABB
{

    private Vector3 minExtents;
    private Vector3 maxExtents;

    /**
     * Creates a new AABB from given min extents and max extents
     */
    public AABB(Vector3 minExtents, Vector3 maxExtents)
    {
        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
    }

    /**
     * Returns infos from intersection between this AABB and given one
     */
    public IntersectionInfos intersectAABB(AABB other)
    {
        Vector3 dist1 = other.getMinExtents().sub(getMaxExtents());
        Vector3 dist2 = getMinExtents().sub(other.getMaxExtents());
        Vector3 dist = Vector3.max(dist1, dist2);
        float maxDistance = dist.max();

        return new IntersectionInfos(maxDistance < 0.f, (float) maxDistance);
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
}
