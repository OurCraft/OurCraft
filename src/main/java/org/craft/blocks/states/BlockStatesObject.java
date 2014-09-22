package org.craft.blocks.states;

import java.util.*;

public class BlockStatesObject
{

    private HashMap<BlockState, IBlockStateValue> map;

    public BlockStatesObject()
    {
        map = new HashMap<BlockState, IBlockStateValue>();
    }

    public BlockStatesObject(BlockState[] states, IBlockStateValue[] values)
    {
        this();
        for(int i = 0; i < states.length; i++ )
        {
            map.put(states[i], values[i]);
        }
    }

    public void set(BlockState state, IBlockStateValue value)
    {
        map.put(state, value);
    }

    public IBlockStateValue get(BlockState state)
    {
        return map.get(state);
    }

    public boolean has(BlockState state)
    {
        return map.containsKey(state);
    }
}
