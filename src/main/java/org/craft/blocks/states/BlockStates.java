package org.craft.blocks.states;

import java.util.*;

public final class BlockStates
{

    public static BlockState                                                   orientation;
    public static BlockState                                                   cableConnexions;

    public static final HashMap<String, BlockState>                            BLOCK_STATES_REGISTRY        = new HashMap<String, BlockState>();
    public static final HashMap<BlockState, HashMap<String, IBlockStateValue>> BLOCK_STATES_VALUES_REGISTRY = new HashMap<BlockState, HashMap<String, IBlockStateValue>>();

    /**
     * Registers all blocks states of the game
     */
    public static void init()
    {
        registerState(orientation = new BaseBlockState("orientation"));
        for(EnumLogBlockStates state : EnumLogBlockStates.values())
        {
            registerValue(orientation, state);
        }

        registerState(cableConnexions = new BaseBlockState("connexions"));
        for(EnumConnexionStates state : EnumConnexionStates.values())
        {
            registerValue(cableConnexions, state);
        }
    }

    /**
     * Registers a block state into the BLOCK_STATE_REGISTRY field
     */
    public static void registerState(BlockState state)
    {
        if(BLOCK_STATES_REGISTRY.containsKey(state.toString()))
        {
            throw new IllegalArgumentException("Id " + state.toString() + " is already used by " + BLOCK_STATES_REGISTRY.get(state.toString()) + " when trying to add " + state);
        }
        BLOCK_STATES_REGISTRY.put(state.toString(), state);
        BLOCK_STATES_VALUES_REGISTRY.put(state, new HashMap<String, IBlockStateValue>());
    }

    /**
     * Returns the block state in BLOCK_STATE_REGISTRY with given id
     */
    public static BlockState getState(String string)
    {
        return BLOCK_STATES_REGISTRY.get(string);
    }

    /**
     * Registers a block state into the BLOCK_STATE_REGISTRY field
     */
    public static void registerValue(BlockState state, IBlockStateValue value)
    {
        if(BLOCK_STATES_VALUES_REGISTRY.containsKey(value.toString()))
        {
            throw new IllegalArgumentException("Id " + value.toString() + " is already used by " + BLOCK_STATES_VALUES_REGISTRY.get(value.toString()) + " when trying to add " + value);
        }
        HashMap<String, IBlockStateValue> map = BLOCK_STATES_VALUES_REGISTRY.get(state);
        map.put(value.toString(), value);
    }

    /**
     * Returns the block state in BLOCK_STATE_REGISTRY with given id
     */
    public static IBlockStateValue getValue(BlockState state, String string)
    {
        return BLOCK_STATES_VALUES_REGISTRY.get(state).get(string);
    }

}
