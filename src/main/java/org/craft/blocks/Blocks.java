package org.craft.blocks;

import java.util.*;

import com.google.common.collect.*;

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
    public static Block                        rose;
    public static Block                        cable;
    public static Block                        powerSource;
    public static Block                        powerDisplay;
    public static Block                        obsidian;
    // ONLY FOR TESTING
    public static Block                        dirtSlab;
    public static final HashMap<String, Block> BLOCK_REGISTRY = Maps.newHashMap();
    private static List<Block>                 blockByID      = Lists.newArrayList();

    /**
     * Registers all blocks of the game
     */
    public static void init()
    {
        register(air = new BlockAir());
        register(dirt = new Block("dirt"));
        register(grass = new Block("grass"));
        register(bedrock = new Block("bedrock"));
        register(stone = new Block("stone"));
        register(log = new BlockLog("log"));
        register(leaves = new BlockTransparent("leaves"));
        register(glass = new BlockTransparent("glass"));
        register(rose = new BlockFlower("rose"));
        register(cable = new BlockCable("copper_cable"));
        register(powerSource = new BlockPowerSource("power_source"));
        register(powerDisplay = new BlockPowerDisplay("power_display"));
        register(obsidian = new Block("obsidian"));
        register(dirtSlab = new BlockHalfSlab("dirt_slab"));

        for(short i = 0; i < blockByID.size(); i++ )
        {
            Block b = blockByID.get(i);
            if(b != null)
                b.setUniqueID(i);
        }
    }

    /**
     * Registers a block into the BLOCK_REGISTRY field
     */
    public static void register(Block block)
    {
        if(BLOCK_REGISTRY.containsKey(block.getId()))
        {
            throw new IllegalArgumentException("Id " + block.getId() + " is already used by " + BLOCK_REGISTRY.get(block.getId()) + " when trying to add " + block);
        }
        BLOCK_REGISTRY.put(block.getId(), block);
        blockByID.add(block);
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

    /**
     * Returns a block depending on its UID
     */
    public static Block getByID(int id)
    {
        Block b = blockByID.get(id);
        if(b == null)
            b = air;
        return b;
    }

    public static List<Block> getBlocks()
    {
        return blockByID;
    }
}
