package org.craft.maths;

public class Plane
{

    private Vector3 normal;
    private Vector3 point;

    public Plane()
    {

    }

    public void setNormalAndPoint(Vector3 normal, Vector3 point)
    {
        this.normal = normal;
        this.point = point;
    }

    public float getDistance(Vector3 point)
    {
        if(point == null || point == null || this.point == null)
            return 0f;
        float a = normal.getX();
        float b = normal.getY();
        float c = normal.getZ();
        float d = -a * this.point.getX() - b * this.point.getY() - c * this.point.getZ(); // Thog, I need help
        float numerator = Vector3.get(a * point.getX(), b * point.getY(), c * point.getZ()).add(d).length();
        float denominator = (float) Math.sqrt(a * a + b * b + c * c);
        return numerator / denominator;
    }

}
