package org.craft.blocks.states;

import java.util.*;

import com.google.common.collect.*;

public class BlockStatesObject implements Iterable<IBlockStateValue>
{

    /**
     * Map containing all block state values for each block state.
     */
    private HashMap<BlockState, IBlockStateValue> map;

    /**
     * Creates an empty BlockStatesObject
     */
    public BlockStatesObject()
    {
        map = Maps.newHashMap();
    }

    /**
     * Creates a BlockStatesObject with given states and values with<br/>
     * values[i] is the value for states[i]
     */
    public BlockStatesObject(BlockState[] states, IBlockStateValue[] values)
    {
        this();
        for(int i = 0; i < states.length; i++ )
        {
            map.put(states[i], values[i]);
        }
    }

    /**
     * Set the value of a block state
     */
    public void set(BlockState state, IBlockStateValue value)
    {
        map.put(state, value);
    }

    /**
     * Returns the value of given state, or null if inexistant.
     */
    public IBlockStateValue get(BlockState state)
    {
        return map.get(state);
    }

    /**
     * Checks if this object has a value for given state
     */
    public boolean has(BlockState state)
    {
        return map.containsKey(state);
    }

    /**
     * Clear internal map containing states and values.
     * <br/>{@link #size()} will return 0 after this operation.
     */
    public void clear()
    {
        map.clear();
    }

    /**
     * Returns internal map containing
     */
    public HashMap<BlockState, IBlockStateValue> getMap()
    {
        return map;
    }

    /**
     * Returns size of internal map.
     */
    public int size()
    {
        return map.size();
    }

    @Override
    public Iterator<IBlockStateValue> iterator()
    {
        return map.values().iterator();
    }
}
