package org.craft.blocks;

public final class Blocks
{

    public static Block dirt;
    public static Block grass;
    public static Block air;
    public static Block bedrock;
    public static Block stone;
    public static Block log;
    public static Block leaves;

    public static void init()
    {
        air = new BlockAir();
        dirt = new Block("dirt");
        grass = new BlockGrass("grass");
        bedrock = new Block("bedrock");
        stone = new Block("stone");
        log = new BlockLog("log");
        leaves = new BlockLeaves("leaves");
    }

    public static Block get(String string)
    {
        if(string == null)
            return dirt;
        return Block.registry.get(string);
    }
}
