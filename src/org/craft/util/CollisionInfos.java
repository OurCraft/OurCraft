package org.craft.util;

public class CollisionInfos
{

    public static enum CollisionType
    {
        BLOCK, ENTITY, NONE;
    }

    public EnumSide      side = EnumSide.TOP;
    public CollisionType type;
    public Object        value;
    public float         x;
    public float         y;
    public float         z;
    public float         distance;

    public CollisionInfos()
    {
        type = CollisionType.NONE;
    }
}
