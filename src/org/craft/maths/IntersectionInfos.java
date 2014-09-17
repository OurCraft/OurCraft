package org.craft.maths;

public class IntersectionInfos
{

	private boolean doesIntersects;
	private float   distance;

	public IntersectionInfos(boolean intersects, float distance)
	{
		this.doesIntersects = intersects;
		this.distance = distance;
	}

	public float getDistance()
	{
		return distance;
	}

	public boolean doesIntersects()
	{
		return doesIntersects;
	}

}
