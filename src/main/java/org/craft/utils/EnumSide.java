package org.craft.utils;

public enum EnumSide
{

    NORTH(0, 0, -1, "north"), SOUTH(0, 0, 1, "south"), EAST(1, 0, 0, "east"), WEST(-1, 0, 0, "west"), TOP(0, 1, 0, "top", "up"), BOTTOM(0, -1, 0, "down", "bottom"), UNDEFINED(0, 0, 0, "none", "undefined");

    static
    {
        NORTH.setOpposite(SOUTH);
        SOUTH.setOpposite(NORTH);
        EAST.setOpposite(WEST);
        WEST.setOpposite(EAST);
        BOTTOM.setOpposite(TOP);
        TOP.setOpposite(BOTTOM);
    }
    private int      x, y, z;
    private String[] aliases;
    private EnumSide opposite;

    EnumSide(int x, int y, int z, String... aliases)
    {
        this.aliases = aliases;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String[] getAliases()
    {
        return aliases;
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

    public static EnumSide fromString(String a)
    {
        for(EnumSide side : values())
        {
            if(side.getAliases() != null)
                for(String alias : side.getAliases())
                {
                    if(alias.equals(a))
                        return side;
                }
        }
        return UNDEFINED;
    }

    void setOpposite(EnumSide opposite)
    {
        this.opposite = opposite;
    }

    public EnumSide opposite()
    {
        return opposite;
    }
}
