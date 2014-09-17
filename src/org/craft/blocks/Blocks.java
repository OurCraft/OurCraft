package org.craft.blocks;

public final class Blocks
{

    public static Block dirt;
    public static Block grass;
    public static Block air;

    public static void init()
    {
        air = new BlockAir();
        dirt = new Block("dirt");
        grass = new BlockGrass("grass");
    }

    public static Block get(String string)
    {
        return Block.registry.get(string);
    }
}
