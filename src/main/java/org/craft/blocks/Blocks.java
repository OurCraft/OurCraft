package org.craft.blocks;

import java.util.*;

public final class Blocks
{

    public static Block                        dirt;
    public static Block                        grass;
    public static Block                        air;
    public static Block                        bedrock;
    public static Block                        stone;
    public static Block                        log;
    public static Block                        leaves;
    public static Block                        glass;
    public static final HashMap<String, Block> BLOCK_REGISTRY = new HashMap<>();

    public static void init()
    {
        register(air = new BlockAir());
        register(dirt = new Block("dirt"));
        register(grass = new BlockGrass("grass"));
        register(bedrock = new Block("bedrock"));
        register(stone = new Block("stone"));
        register(log = new BlockLog("log"));
        register(leaves = new BlockTransparent("leaves"));
        register(glass = new BlockTransparent("glass"));
    }

    /**
     * Registers a block into the BLOCK_REGISTRY field
     */
    public static void register(Block block)
    {
        if(BLOCK_REGISTRY.containsKey(block.getID()))
        {
            throw new IllegalArgumentException("Id " + block.getID() + " is already used by " + BLOCK_REGISTRY.get(block.getID()) + " when trying to add " + block);
        }
        BLOCK_REGISTRY.put(block.getID(), block);
    }

    /**
     * Returns the block in BLOCK_REGISTRY with given id
     */
    public static Block get(String string)
    {
        if(string == null)
            return air;
        return BLOCK_REGISTRY.get(string);
    }
}
