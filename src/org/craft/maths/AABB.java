package org.craft.maths;

public class AABB
{

    private Vector3 minExtents;
    private Vector3 maxExtents;

    public AABB(Vector3 minExtents, Vector3 maxExtents)
    {
        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
    }

    public IntersectionInfos intersectAABB(AABB other)
    {
        Vector3 dist1 = other.getMinExtents().sub(getMaxExtents());
        Vector3 dist2 = getMinExtents().sub(other.getMaxExtents());
        Vector3 dist = Vector3.max(dist1, dist2);
        double maxDistance = dist.max();

        return new IntersectionInfos(maxDistance < 0, (float)maxDistance);
    }

    public Vector3 getMinExtents()
    {
        return minExtents;
    }

    public Vector3 getMaxExtents()
    {
        return maxExtents;
    }

    public AABB translate(Vector3 position)
    {
        return new AABB(minExtents.add(position), maxExtents.add(position));
    }
}
