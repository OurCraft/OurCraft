package org.craft.utils;

public enum EnumSide
{

    NORTH(0, 0, -1), SOUTH(0, 0, 1), EAST(1, 0, 0), WEST(-1, 0, 0), TOP(0, 1, 0), BOTTOM(0, -1, 0), UNDEFINED(0, 0, 0);

    private int x, y, z;

    EnumSide(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getTranslationX()
    {
        return x;
    }

    public int getTranslationY()
    {
        return y;
    }

    public int getTranslationZ()
    {
        return z;
    }
}
